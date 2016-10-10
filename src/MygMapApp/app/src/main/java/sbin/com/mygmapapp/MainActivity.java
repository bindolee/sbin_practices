package sbin.com.mygmapapp;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback {

    private static final int GPS_ERRORDIALOG_REQUEST = 9001;
    private static final String LOGTAG = "MainActivity";
    private static final double SEATTLE_LAT = 47.60621,
        SEATTLE_LNG=-122.33207,
        SYDNEY_LAT=-33.867487,
        SYDNEY_LAN=151.20699,
        NEWYORK_LAT=40.714353,
        NEWYORK_LNG=-74.005973,
        SANDIEGO_LAT=32.954561,
        SANDIEGO_LNG=-117.138611;

    private static final boolean USES_POLYGONLINE = false;
    private static final boolean USES_POLYGON = false;
    private static final boolean USES_CIRCLE = true;

    private static final float DEFAULT_ZOOM = 17;
    Marker mMarker1;
    Marker mMarker2;
    ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    Marker mMarker_USE_CIRCLE;
    Circle mCircle;

    private static final int POLYGON_POINTS = 3;

    Polygon     mShape;
    Polyline    mLine;

    GoogleMap mMap; // need to add compile dependencis on build.gradle by compile import com.google.android.gms:play-services-maps:9.6.1

    private static final boolean USE_MAPVIEW = false;
    private static boolean SIGNAL = true; // need this semaphore signal from onMapready call back function
    MapView mMapView;
    GoogleApiClient mGoogApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (servicesOK()){
            // this way to use mapview xml
            if (USE_MAPVIEW) {
                setContentView(R.layout.activity_map_view);
                mMapView = (MapView) findViewById(R.id.map_view);
                mMapView.onCreate(savedInstanceState);
            }
            else{
                //this is to use fragmentactivity
                setContentView(R.layout.activity_map);

                // Need semphore or some sync thing from initMap when onMyMap ready call back function is called
                initMap();
            }
        }
        else{
            setContentView(R.layout.activity_main);
        }


    }

    /* This is needed since OnMapReadyCallback need call back function from initMap() */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        SIGNAL = true; // need some signal like semaphore or busywait -- think about this later.

        if (mMap == null) {
            Toast.makeText(this, "Map Not Available", Toast.LENGTH_SHORT).show();
            return;
        }

        //Way to create customer maker window information.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.info_window, null);
                TextView tvlocal = (TextView) v.findViewById(R.id.tv_locality);
                TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                TextView tvSnip = (TextView) v.findViewById(R.id.tv_snippet);

                LatLng ll = marker.getPosition();

                tvlocal.setText(marker.getTitle());
                tvLat.setText("Latitue: "+ ll.latitude);
                tvLng.setText("Longitude: "+ ll.longitude);
                tvSnip.setText(marker.getSnippet());

                return v;
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng ll) {
                Geocoder gc = new Geocoder(MainActivity.this);
                List<Address> list = null;
                try {
                    list = gc.getFromLocation(ll.latitude,ll.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Address addr = list.get(0);
                MainActivity.this.setMarker(addr.getLocality(), addr.getCountryName(),
                       ll.latitude,ll.longitude);

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String msg = marker.getTitle() + " (" + marker.getPosition().latitude +
                        ", " + marker.getPosition().longitude + ")";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        //Marker drag event listener
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Geocoder gc = new Geocoder(MainActivity.this);
                List<Address> list = null;
                LatLng ll = marker.getPosition();

                try {
                    list = gc.getFromLocation(ll.latitude,ll.longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                Address addr = list.get(0);
                marker.setTitle(addr.getLocality());
                marker.setSnippet(addr.getCountryName());
                marker.showInfoWindow();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }
        });

        Toast.makeText(this, "Ready to Map", Toast.LENGTH_SHORT).show();
        //mMap.setMyLocationEnabled(true); // show my current location gps bar in the map..
        // Need to debug this line to get the current location.. run time exception.
/*        if (mGoogApiClient == null) {
            mGoogApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    //.addApi(Locati)
                    //.addApi(LocationServices.API)
                    .build();
        }
        mGoogApiClient.connect();*/
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        //MapsInitializer.initialize(this);
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,zoom);

        if (update != null && mMap != null)
            mMap.moveCamera(update);
    }

    private void gotoLocation(double lat, double lng){
        LatLng ll = new LatLng(lat,lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);

        if (update != null && mMap != null)
            mMap.moveCamera(update);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public boolean servicesOK(){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS){
            Log.i(LOGTAG,"Connection sucess here");
            return true;
        }
        else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,this,GPS_ERRORDIALOG_REQUEST);
            dialog.show();
            Log.i(LOGTAG,"Google Service Not available here, Need KEY or something");
        }
        else{
            Toast.makeText(this, "Can't Connect to google play map services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void initMap()  {

        if (mMap == null){
            SupportMapFragment mapFragment =
                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync((OnMapReadyCallback) this);
        }
        Log.i(LOGTAG,"InitMap funciton");
        //return (mMap != null);
    }

    public void geoLocate(View view) throws IOException {
        EditText et = (EditText) findViewById(R.id.editText1);
        String location = et.getText().toString();
        if (location.length() == 0){
            Toast.makeText(this, "Please enter a location!!", Toast.LENGTH_LONG).show();
            return;
        }

        //Hide soft key
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location,1);
        Address addr = list.get(0); // get the 1st only address.
        String locality = addr.getLocality();

        //Toast.makeText(this,locality , Toast.LENGTH_LONG).show();

        double lat = addr.getLatitude();
        double lng = addr.getLongitude();
        String country = addr.getCountryName();

        gotoLocation(lat,lng,DEFAULT_ZOOM);
        Toast.makeText(this, locality+ ", Lat: "+lat+" ,Lng: "+lng, Toast.LENGTH_LONG).show();

        setMarker(locality, country, lat, lng);
    }

    private void setMarker(String locality, String country, double lat, double lng) {
        LatLng ll = new LatLng(lat,lng);
        if(USES_POLYGON) {
            if (mMarkers.size() == POLYGON_POINTS) {
                removeEverything();
            }
        }
        //Marker options uses builder type constructor
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(ll)
                .anchor(.5f,.5f) // 0.0  ~ 1.0 .. so .5 is the center..
 //               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mapmarker))
                .draggable(true); // this makes marker draggble.

        if (country.length() >0 ){
            options.snippet(country);
        }

        if(USES_POLYGON) {
            mMarkers.add(mMap.addMarker(options));
            if (mMarkers.size() == POLYGON_POINTS) {
                drawPolygon();
            }
        }
        else if (USES_POLYGONLINE) {
            if (mMarker1 == null) {
                mMarker1 = mMap.addMarker(options);
            } else if (mMarker2 == null) {
                mMarker2 = mMap.addMarker(options);
                drawLine();
            } else {
                removeEverything();
                mMarker1 = mMap.addMarker(options);
                mCircle = drawCircle(ll);
            }
        }
        else if (USES_CIRCLE){
            // you need to save the shape circle to preferences..if you want persistent data.
            if (mMarker_USE_CIRCLE != null){
                removeEverything();
            }
            mMarker_USE_CIRCLE = mMap.addMarker(options);
            mCircle = drawCircle(ll);
        }
        //mMarker = mMap.addMarker(options); // need to track the marker..in mamstatemanager too.
    }

    private Circle drawCircle(LatLng ll) {
        CircleOptions options = new CircleOptions()
                .center(ll)
                .radius(1000)
                .fillColor(0x330000FF) // transparent color
                .strokeColor(Color.BLUE)
                .strokeWidth(3);

        return mMap.addCircle(options);
    }

    private void drawLine() {
        PolylineOptions options = new PolylineOptions()
                .add(mMarker1.getPosition())
                .add(mMarker2.getPosition())
                .color(Color.BLUE)
                .width(5); // default is 10

        mLine = mMap.addPolyline(options);
    }

    private void removeEverything() {
        if (USES_POLYGONLINE) {
            mMarker1.remove();
            mMarker1 = null;
            mMarker2.remove();
            mMarker2 = null;
            mLine.remove();
        }
        else if (USES_POLYGON) {
            for (Marker marker : mMarkers) {
                marker.remove();
            }
            mMarkers.clear();

            mShape.remove();
            mShape = null;
        }
        else if (USES_CIRCLE) {
            mMarker_USE_CIRCLE.remove();
            mMarker_USE_CIRCLE = null;
            mCircle.remove();
            mCircle = null;
        }
    }

    private void drawPolygon(){
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x33000FF)
                .strokeWidth(3)
                .strokeColor(Color.BLUE);

        for (int i=0;i<POLYGON_POINTS;i++){
            options.add(mMarkers.get(i).getPosition());
        }
        mShape = mMap.addPolygon(options);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSattelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.gotoCurrentLocation:
                gotoCurrentLocation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoCurrentLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (locationManager != null){
            Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (currentLocation == null){
                Toast.makeText(this,"Current location isn't available", Toast.LENGTH_SHORT).show();;
            }
            else{
                LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,DEFAULT_ZOOM);
                mMap.animateCamera(update);
            }
        }
/*        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogApiClient);
        if (currentLocation == null){
            Toast.makeText(this,"Current location isn't available", Toast.LENGTH_SHORT).show();;
        }
        else {
            LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll,DEFAULT_ZOOM);
            mMap.animateCamera(update);
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MapStateManager mgr = new MapStateManager(this);
        CameraPosition position = mgr.getSavedCameraPosition(); // For somereason, this gets null.!! on resume state.b
        if (position != null) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            if (mMap != null)
                mMap.moveCamera(update);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this, "Connected to location service", Toast.LENGTH_SHORT).show();
        //to do how to update the location.. locationRequest is depreciated.
     }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = "location: " + location.getLatitude() +", " + location.getLongitude();
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /*  these override needed for mapView--USE_MAPVIEW
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }*/
}

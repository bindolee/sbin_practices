package sbin.com.mygmapapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sbin on 10/8/2016.
 */

@SuppressWarnings("unused")
public class MapStateManager {
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String ZOOM = "zoom";
    private static final String BEARING = "bearing";
    private static final String TILT = "tilt";
    private static final String MAPTYPE = "maptype";

    private static final String PREFS_NAME="mapCameraState";
    private SharedPreferences mapStatePrefs;

    public MapStateManager(Context context){
        mapStatePrefs = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
    }

    public void saveMapState(GoogleMap map){
        SharedPreferences.Editor editor = mapStatePrefs.edit();
        CameraPosition position = map.getCameraPosition();
        editor.putFloat(LATITUDE, (float) position.target.latitude);
        editor.putFloat(LONGITUDE, (float) position.target.longitude);
        editor.putFloat(ZOOM, (float) position.zoom);
        editor.putFloat(BEARING, (float) position.bearing);
        editor.putFloat(TILT, (float) position.tilt);
        editor.putInt(MAPTYPE, map.getMapType());
    }

    public CameraPosition getSavedCameraPosition() {
        double latitue = mapStatePrefs.getFloat(LATITUDE,0);
        if (latitue == 0) {
            return null;
        }

        double longitude = mapStatePrefs.getFloat(LONGITUDE,0);
        if (longitude == 0) {
            return null;
        }

        LatLng target = new LatLng(latitue,longitude);
        float zoom = mapStatePrefs.getFloat(ZOOM,0);
        float bearing = mapStatePrefs.getFloat(BEARING,0);
        float tilt = mapStatePrefs.getFloat(TILT,0);

        CameraPosition position = new CameraPosition(target, zoom, bearing, tilt);
        return position;
    }
}

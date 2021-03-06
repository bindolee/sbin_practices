package sbin.com.webserviceapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import sbin.com.webserviceapp.model.Flower;

// This shows how to handle http using Volley instead of HTTP manger
public class MainActivity extends AppCompatActivity {

    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";

    public static final String ENDPOINT =
            "http://services.hanselandpetal.com";

    ProgressBar pb; // progress bar
    ListView listView;

    List<Flower> flowerList;
    FlowerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the ProgressBar reference by findViewByid
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        //void setVisibility (int visibility) -- visibility	int: One of VISIBLE, INVISIBLE, or GONE.
        pb.setVisibility(View.INVISIBLE); // Same as INVISITIBLE after import View.

        UpdateDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main_option){
            if (isOnline()){
                requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                //requestData("http://services.hanselandpetal.com/feeds/flowers.json");
                //requestData("http://services.hanselandpetal.com/feeds/flowers.xml");
            }
            else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
;            }

        }
        return super.onOptionsItemSelected(item);
    }

    //Using Retrofit instead of Async task - Http handler
    private void requestData(String uri) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .build();

        FlowersAPI api = retrofit.create(FlowersAPI.class);
        api.getFeed(new Callback<List<Flower>>() {
            @Override
            public void onResponse(Call<List<Flower>> call, Response<List<Flower>> response) {
                flowerList = (List<Flower>) call;
                UpdateDisplay();
            }

            @Override
            public void onFailure(Call<List<Flower>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void UpdateDisplay() {
        adapter = new FlowerAdapter(this,R.layout.item_flower,flowerList);
        listView = (ListView) findViewById(R.id.main_listview1);

        //Please check the error condition. on 1st time when app is launching,
        //flowerList has nothing .null.. so.. in this case don't do any display otherwise, it will crash
        if (flowerList != null) {
            if (adapter != null && adapter.getCount() > 0) {
                listView.setAdapter(adapter);
            }
        }
/*        if (flowerList != null){
            for (Flower flower: flowerList) {
                output.append(flower.getName() + "\n");
            }
        }*/

    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else {
            return false;
        }
    }
}

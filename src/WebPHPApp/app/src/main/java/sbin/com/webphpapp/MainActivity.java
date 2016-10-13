package sbin.com.webphpapp;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    TextView output;
    ProgressBar pb;
    List<MyTask> task;

    public static final String PHOTOS_BASE_URL =
            "http://services.hanselandpetal.com/photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.textView);
        output.setMovementMethod(new ScrollingMovementMethod());

        //Set up the ProgressBar reference by findViewByid
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        //void setVisibility (int visibility) -- visibility	int: One of VISIBLE, INVISIBLE, or GONE.
        pb.setVisibility(View.INVISIBLE); // Same as INVISITIBLE after import View.

        //Instanciate List object here.. java7 style syntax
        task = new ArrayList<>();
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
            if (isOnline()) {
                requestData("http://services.hanselandpetal.com/restfuljson.php");
            }
            else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
                ;            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void requestData(String uri) {
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod("POST");
        requestPackage.setUri(uri);
        requestPackage.setParam("param1", "Value 1");
        requestPackage.setParam("param2", "Value 2");
        requestPackage.setParam("param3", "Value 3");
        requestPackage.setParam("param4", "Value 4");
        requestPackage.setParam("name", "Sungwon");
        requestPackage.setParam("price", "23.21");

        //Initialise the MyTask(Async ) here and execute it...
        MyTask task = new MyTask();
        task.execute(requestPackage);
    }

    public void UpdateDisplay(String result) {
        output.append(result + "\n");
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

    //android.os.AsyncTask<Params, Progress, Result>
    private class MyTask extends AsyncTask<RequestPackage, String, String> {

        //This execute before doInBackground() and this has access to the main thread
        @Override
        protected void onPreExecute() {
            //           UpdateDisplay("Starting Task!");

            // Simple way to fix progress bar when there is parall task is running
            if (task.size() == 0) {
                pb.setVisibility(View.VISIBLE);
            }
            task.add(this);
        }

        //doInbackground can't be accessed from user interface or main thread
        // or access mainthread method.. Access main thread method only can be doable
        // from pre, post , porgressupdate execute..etc
        @Override
        protected String doInBackground(RequestPackage... params) {

            String content = HttpManager.getData(params[0]);
            return content;
        }

        //This execute after doInBackground() and this has access to the main thread
        @Override
        protected void onPostExecute(String result) {

            // Simple way to fix progress bar when there is parallel task is running
            task.remove(this);
            if (task.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if (result == null){
                //you can't pass this instance, since you are talking to
                // main thread/activity's current instance from async task
                Toast.makeText(MainActivity.this,"Can't connect to web service",Toast.LENGTH_LONG).show();
                return;
            }

            UpdateDisplay(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            /*for (int i=0; i<values.length; i++){
                UpdateDisplay(values[i]);
            }*/
            //           UpdateDisplay(values[0]);

        }
    }
}

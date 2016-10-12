package sbin.com.webserviceapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView output;
    ProgressBar pb; // progress bar
    List<MyTask> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the TextView reference by findViewByid
        output = (TextView) findViewById(R.id.main_textview1);
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
            //Initialise the MyTask(Async ) here and execute it...
            MyTask task = new MyTask();

/*            //.execute excute task as serially.. we need parallel execite.. serial is primitive way
            task.execute("Param 1", "Param 2", "Param 3", "Param 4", "Param 5", "Param 6");*/

            //Parallel way of executing async thread. .. don't choose serial way
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Param 1", "Param 2", "Param 3", "Param 4", "Param 5", "Param 6");
        }
        return super.onOptionsItemSelected(item);
    }

    public void UpdateDisplay(String message) {
        output.append(message + "\n");
    }

    //android.os.AsyncTask<Params, Progress, Result>
    private class MyTask extends AsyncTask<String, String, String> {

        //This execute before doInBackground() and this has access to the main thread
        @Override
        protected void onPreExecute() {
            UpdateDisplay("Starting Task!");

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
        protected String doInBackground(String... params) {

            //This is getting progress on each input param and can be retrieved onProgressUpdate function
            for (int i=0; i<params.length; i++){
                publishProgress("Working with" + params[i]);

                //Mimic as if this thread takes long like 1 sec... -- sleep 1sec(pause 1sec)
                try {
                    Thread.sleep(1000); // sleep 1000 ms = 1 sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //this returns string since <param1, param2, String> , the last paremeter(string) is return type
            return "Task Completed!";
        }

        //This execute after doInBackground() and this has access to the main thread
        @Override
        protected void onPostExecute(String result) {
            UpdateDisplay(result);

            // Simple way to fix progress bar when there is parallel task is running
            task.remove(this);
            if (task.size() == 0) {
                pb.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            /*for (int i=0; i<values.length; i++){
                UpdateDisplay(values[i]);
            }*/
            UpdateDisplay(values[0]);

        }
    }
}

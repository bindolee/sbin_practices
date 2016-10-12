package sbin.com.simplefragmentandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import sbin.com.simplefragmentandroid.data.Flower;

public class MainActivity extends AppCompatActivity
    implements FlowerListFragment.Callbacks {

    public static final String FLOWER_BUNDLE = "FLOWER_BUNDLE";
    private static final int REQUEST_CODE = 1001;
    private static final int REQUEST_CODE2 = 1002;
    private boolean isTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detailContainer) != null){
            //this means, this is tablet(large screen)
            isTwoPane = true;
        }

/*        FlowerListFragment frag = new FlowerListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.myMainFrameContainer, frag)
                .commit();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.main_option){
/*            ScreenUtility utility = new ScreenUtility(this);
            String output = "Width: " + utility.getWidth() +", " +
                    "Height: " + utility.getHeight();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(output)
                    .setTitle("Dimensions")
                    .create()
                    .show();*/
            Intent intent = new Intent();
            intent.setClass(this, MyPrefsActivity.class);
            startActivityForResult(intent,REQUEST_CODE2);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Flower flower) {
        Bundle b = flower.toBundle();

        if (isTwoPane){
            // if this is a tablet
            FlowerDetailFragment fragment = new FlowerDetailFragment();
            fragment.setArguments(b);
            getFragmentManager().beginTransaction()
                    .replace(R.id.detailContainer,fragment)
                    .commit();
        }
        else {
            Intent intent = new Intent(this, FlowerDetailActivity.class);
            intent.putExtra(FLOWER_BUNDLE, b);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE2){
            SharedPreferences myPrefs =
                    PreferenceManager.getDefaultSharedPreferences(this);
            boolean pref1 = myPrefs.getBoolean("pref1", false);
            //Toast.makeText(this,"Preference: "+pref1,Toast.LENGTH_SHORT).show();
;
            MyDialogFragment dialog = new MyDialogFragment();
            Bundle b = new Bundle();
            b.putString("message","Preference: "+pref1);
            dialog.setArguments(b);

            dialog.show(getFragmentManager(),"MyDialog");
        }
    }
}

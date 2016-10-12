package sbin.com.simplefragmentandroid;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by sbin on 10/11/2016.
 */

public class MyPrefsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPrefsFragment())
                .commit();
    }
}

package sbin.com.simplefragmentandroid;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by sbin on 10/11/2016.
 */

public class MyPrefsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}

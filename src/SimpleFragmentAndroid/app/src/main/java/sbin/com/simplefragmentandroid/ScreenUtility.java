package sbin.com.simplefragmentandroid;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

// This gives device independent pixel.. after calculation based on the hw.
public class ScreenUtility {

    private Activity activity;
    private float dpWidth;
    private float dpHeight;

    public ScreenUtility(Activity activity) {
        this.activity = activity;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = activity.getResources().getDisplayMetrics().density;
        dpHeight = outMetrics.heightPixels / density;
        dpWidth = outMetrics.widthPixels / density;
    }

    public float getWidth() {
        return dpWidth;
    }

    public float getHeight() {
        return dpHeight;
    }

}

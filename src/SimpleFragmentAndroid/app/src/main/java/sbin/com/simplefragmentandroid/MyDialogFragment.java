package sbin.com.simplefragmentandroid;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by sbin on 10/11/2016.
 */

public class MyDialogFragment extends DialogFragment {
    public MyDialogFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Result")
                //.setMessage("This is the result")
                .setMessage(b.getString("message"))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                });

        return builder.create();
    }
}

package plainoldnotes.sbin.com.notetakingoldandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import plainoldnotes.sbin.com.notetakingoldandroidapp.data.NoteItem;

import static plainoldnotes.sbin.com.notetakingoldandroidapp.MainActivity.ITEM_KEY;
import static plainoldnotes.sbin.com.notetakingoldandroidapp.MainActivity.ITEM_TEXT;

/**
 * Created by sbin on 10/6/2016.
 */

public class NoteEditorActivity extends AppCompatActivity {
    private NoteItem note;
    private final static String LOGTAG = "NoteEditor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        //getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = this.getIntent();
        note = new NoteItem();
        note.setKey(intent.getStringExtra("key"));
        note.setText(intent.getStringExtra("text"));

        EditText et = (EditText) findViewById(R.id.noteText);
        et.setText(note.getText());
        et.setSelection(note.getText().length());
    }

    private void saveAndFinish(){
        EditText et = (EditText) findViewById(R.id.noteText);
        String noteText = et.getText().toString();

        Intent intent = new Intent();
        intent.putExtra(ITEM_KEY, note.getKey());
        intent.putExtra(ITEM_TEXT, noteText);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndFinish();
            /*Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                // This activity is NOT part of this app's task, so create a new task
                // when navigating up, with a synthesized back stack.
                TaskStackBuilder.create(this)
                        // Add all of this activity's parents to the back stack
                        .addNextIntentWithParentStack(upIntent)
                        // Navigate up to the closest parent
                        .startActivities();
            } else {
                // This activity is part of this app's task, so simply
                // navigate up to the logical parent activity.
                NavUtils.navigateUpTo(this, upIntent);
            }*/
            Log.i(LOGTAG,"Save and Finish called on <- button");
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        saveAndFinish();
        Log.i(LOGTAG,"Save and Finish called on back button");
    }
}

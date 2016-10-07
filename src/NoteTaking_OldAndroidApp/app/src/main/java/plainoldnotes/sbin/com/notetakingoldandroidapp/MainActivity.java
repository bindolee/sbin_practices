package plainoldnotes.sbin.com.notetakingoldandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import plainoldnotes.sbin.com.notetakingoldandroidapp.data.NoteDataSource;
import plainoldnotes.sbin.com.notetakingoldandroidapp.data.NoteItem;

//public class MainActivity extends ListActivity {
public class MainActivity extends AppCompatActivity {

    public static final int EDITOR_ACTIVITY_REQUEST = 1001;
    public static final int MENU_DELETE_ID = 1002;
    public static final String ITEM_KEY = "key";
    public static final String ITEM_TEXT = "text";

    private int currentNoteId;
    private final static String LOGTAG = "MainActivity";

    private NoteDataSource dataSource;
    List<NoteItem> notesList;
    private ListView lv;

    private Intent intentforNoteEditActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOGTAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher); //set icon on tool bar

/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        intentforNoteEditActivity = new Intent(this, NoteEditorActivity.class);

        lv = (ListView) findViewById(R.id.list_content);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteItem note = notesList.get(position);
                intentforNoteEditActivity.putExtra(ITEM_KEY, note.getKey());
                intentforNoteEditActivity.putExtra(ITEM_TEXT, note.getText());
                startActivityForResult(intentforNoteEditActivity, EDITOR_ACTIVITY_REQUEST);
                Log.i(LOGTAG, "onCreate::onItemClicklistener");

            }
        });

        dataSource = new NoteDataSource(this);
        refreshDisplay();


    }

    private void refreshDisplay() {
        notesList = dataSource.findAll();
        ArrayAdapter<NoteItem>adapter =
                new ArrayAdapter<NoteItem>(this, R.layout.list_item_layout, notesList);
        //setListAdapter(adapter);
        lv.setAdapter(adapter);
        Log.i(LOGTAG, "refreshDisplay");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Log.i(LOGTAG, "onCreateOptionMenu");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i(LOGTAG, "onOptionsItemSelected");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            createNote();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void createNote() {
        Log.i(LOGTAG, "createNote");
        NoteItem note = NoteItem.getNew();
        intentforNoteEditActivity.putExtra(ITEM_KEY, note.getKey());
        intentforNoteEditActivity.putExtra(ITEM_TEXT, note.getText());
        startActivityForResult(intentforNoteEditActivity, EDITOR_ACTIVITY_REQUEST);
    }

    // Call back function from createNote(startActivityForResult)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK){
            NoteItem note = new NoteItem();
            note.setKey(data.getStringExtra(ITEM_KEY));
            note.setText(data.getStringExtra(ITEM_TEXT));
            dataSource.update(note);
            refreshDisplay();
            Log.i(LOGTAG, "onActivityResult::Refresh Disp ok, Result code is " + resultCode);
        }
        else{
            Log.i(LOGTAG, "onActivityResult::Refresh Disp not ok, Result code is " + resultCode);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId = (int)info.id;
        menu.add(0,MENU_DELETE_ID,0,R.string.delete);
        Log.i(LOGTAG, "onCreateContextMenu");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_DELETE_ID){
            NoteItem note = notesList.get(currentNoteId);
            dataSource.remove(note);
            refreshDisplay();
        }
        Log.i(LOGTAG, "onContextItemSelected");
        return super.onContextItemSelected(item);
    }

}

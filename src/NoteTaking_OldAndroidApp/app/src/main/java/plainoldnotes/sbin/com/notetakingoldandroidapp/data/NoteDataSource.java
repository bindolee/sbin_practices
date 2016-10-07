package plainoldnotes.sbin.com.notetakingoldandroidapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by sbin on 9/30/2016.
 */

public class NoteDataSource {

    private static final String PREFKEY = "notes";
    private SharedPreferences notesPrefs;

    public NoteDataSource(Context context){
        notesPrefs = context.getSharedPreferences(PREFKEY,Context.MODE_PRIVATE);
    }

    public List<NoteItem> findAll(){
        Map<String, ?> notesMap = notesPrefs.getAll();

        SortedSet<String> keys= new TreeSet<String>(notesMap.keySet());
        List<NoteItem> noteList = new ArrayList<NoteItem>();
        for (String key: keys){
            NoteItem note = new NoteItem();
            note.setKey(key);
            note.setText((String) notesMap.get(key));
            noteList.add(note);
        }

        return noteList;
    }

    public boolean update(NoteItem note){
        SharedPreferences.Editor editor = notesPrefs.edit();
        editor.putString(note.getKey(),note.getText());
        editor.apply();
        return true;
    }

    public boolean remove(NoteItem note){
        if (notesPrefs.contains(note.getKey())) {
            SharedPreferences.Editor editor = notesPrefs.edit();
            editor.remove(note.getKey());
            editor.apply();
        }
        return true;
    }

}

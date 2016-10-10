package plainoldnotes.sbin.com.notetakingoldandroidapp.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sbin on 9/30/2016.
 */

public class NoteItem {
    private String key;
    private String text;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static NoteItem getNew() {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);

        String pattern = "yyyy-MM-dd HH:mm:ss Z";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        String key = formatter.format(new Date());

        NoteItem note = new NoteItem();
        note.setKey(key);
        note.setText("");
        return note;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
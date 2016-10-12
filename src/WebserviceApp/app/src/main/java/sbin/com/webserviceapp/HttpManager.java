package sbin.com.webserviceapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sbin on 10/12/2016.
 */

public class HttpManager {
    private static final String LOG_TAG = "HttpManager class";
    private static final boolean USES_DEBUG = false;

    public static String getData(String uri) {

        BufferedReader reader = null;

        try {
            // Ready to connect this uri/url by openconnection.
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // Connection is on. then build the reader input stream
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }

            if (USES_DEBUG) {
                if (sb.toString() != null) {
                    Log.i(LOG_TAG, "MSG: " + sb.toString());
                }
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null; // if you put this after if statement, it will return null
                }
            }
        }
    }

/*    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }*/
}

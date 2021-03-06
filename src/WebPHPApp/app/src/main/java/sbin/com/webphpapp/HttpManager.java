package sbin.com.webphpapp;

import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    //Method overload...
    public static String getData(String uri, String userName, String password) {

        BufferedReader reader = null;
        byte[] loginBytes = (userName + ":" + password).getBytes();
        StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes,Base64.DEFAULT));
        HttpURLConnection con = null;

        try {
            // Ready to connect this uri/url by openconnection.
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();

            con.addRequestProperty("Authorization", loginBuilder.toString());

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
            //This try -catch needed in case of authentication is failed..
            try {
                int status = con.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

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

    public static String getData(RequestPackage requestPackage) {

        BufferedReader reader = null;
        String uri = requestPackage.getUri();
        if (requestPackage.getMethod().equals("GET")){
            uri += "?" + requestPackage.getEncodedParams();
        }

        try {
            // Ready to connect this uri/url by openconnection.
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestPackage.getMethod());

            JSONObject json = new JSONObject(requestPackage.getParams());
            String params = "params" + json.toString();

            if (requestPackage.getMethod().equals("POST")){
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(params);
                writer.flush();
            }

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

}

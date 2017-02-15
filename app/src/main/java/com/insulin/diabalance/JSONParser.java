package com.insulin.diabalance;

/**
 * Created by risha95 on 2016-09-24.
 */
import android.os.StrictMode;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;




public class JSONParser {

    public JSONParser() {

    }

    public static String getJSONFromUrl(URL url) {
        // Making HTTP request
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .detectDiskReads().detectDiskWrites().detectNetwork()

                .penaltyLog().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()

                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()

                .penaltyLog().penaltyDeath().build());

        StringBuilder sb = new StringBuilder();
        try {
            url.openStream();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
                conn.setConnectTimeout(2000);
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    while (true) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        sb.append(line + "\n");
                    }
                    br.close();
                } else {
                    Toast.makeText(null, "http_not", Toast.LENGTH_LONG).show();
                }
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return JSON String
        return sb.toString();
    }
}

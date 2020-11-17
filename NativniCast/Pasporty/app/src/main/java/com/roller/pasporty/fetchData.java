package com.roller.pasporty;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class fetchData extends AsyncTask<String,Void,String> {
    @Override
    public String doInBackground(String... urls) {
        String result = null;
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result = result + line;
            }
            conn.disconnect();
        } catch (Exception e) {
            Log.e("ERROR FETCHING", e.toString());
        }
        return result;
    }
}

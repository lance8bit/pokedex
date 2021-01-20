package com.example.pokedex;


import android.os.AsyncTask;
import android.util.Log;

import com.ahmadrosid.svgloader.SvgLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class fetchDataType extends AsyncTask<Void, Void, Void> {

    protected String data = "";
    protected String results = "";
    protected ArrayList<String> strTypes; // Create an ArrayList object

    private WeakReference<MainActivity> mainActivityWeakReference;

    public fetchDataType(MainActivity context) {
        mainActivityWeakReference = new WeakReference<>(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //Make API connection
            URL url = new URL("https://pokeapi.co/api/v2/type/");
            Log.i("logtest", "https://pokeapi.co/api/v2/type/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // Read API results
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sBuilder = new StringBuilder();

            // Build JSON String
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            inputStream.close();
            data = sBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid){
        JSONObject jObject = null;
        String types;
        JSONArray jArray = null;

        try {
            jObject = new JSONObject(data);
            jArray = jObject.getJSONArray("results");

            MainActivity mainActivity = mainActivityWeakReference.get();
            mainActivity.typesList.clear();

            for (int i = 0; i < jArray.length(); ++i) {
                JSONObject rec = jArray.getJSONObject(i);

                mainActivity.typesList.add(rec.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

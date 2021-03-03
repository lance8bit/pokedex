package com.example.pokedex;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class fetchPokesTypes extends AsyncTask<Void, Void, Void> {

    protected String data = "";
    protected String typeSearch;
    protected ArrayList<String> pokesType; // Create an ArrayList object

    private WeakReference<MainActivity> mainActivityWeakReference;

    public fetchPokesTypes(String pokesType, MainActivity context) {
        mainActivityWeakReference = new WeakReference<>(context);
        this.typeSearch = pokesType;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try {
            //Make API connection
            URL url = new URL("https://pokeapi.co/api/v2/type/"+typeSearch);
            Log.i("logtest", "https://pokeapi.co/api/v2/type/"+typeSearch);
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
    protected void onPostExecute(Void aVoid) {
        JSONObject jObject = null;
        JSONObject subjObject = null;
        JSONArray jsonArray = null;
        JSONArray subjArray = null;

        try {
            jObject = new JSONObject(data);
            jsonArray = jObject.getJSONArray("pokemon");

            MainActivity mainActivity = mainActivityWeakReference.get();
            mainActivity.pokeTypeSel.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject rec = jsonArray.getJSONObject(i);

                subjArray = rec.getJSONArray("pokemon");

                for (int j = 0; j < subjArray.length(); j++) {
                    subjObject = subjArray.getJSONObject(i);
                    mainActivity.pokeTypeSel.add(subjObject.getString("name"));
                }
            }

            for (int i = 0; i < mainActivity.pokeTypeSel.size(); i++) {
                Log.i("POKEIN", "onPostExecute: " + mainActivity.pokeTypeSel.get(i));
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

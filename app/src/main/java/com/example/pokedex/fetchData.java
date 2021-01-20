package com.example.pokedex;


import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.ahmadrosid.svgloader.SvgLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class fetchData extends AsyncTask<Void, Void, Void> {

    protected String data = "";
    protected String results = "";
    protected ArrayList<String> strTypes; // Create an ArrayList object
    protected String pokSearch;
    protected String curPokeId;


    private WeakReference<MainActivity> mainActivityWeakReference;

    public fetchData(String pokSearch, MainActivity context) {
        mainActivityWeakReference = new WeakReference<>(context);
        this.pokSearch = pokSearch;
        strTypes = new ArrayList<String>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            //Make API connection
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokSearch);
            Log.i("logtest", "https://pokeapi.co/api/v2/pokemon/" + pokSearch);
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
        String img = "";
        String typeName = "";
        String typeObj="";

        try {
            jObject = new JSONObject(data);

            // Get JSON name, height, weight
            results += "Name: " + jObject.getString("name").toUpperCase() + "\n" +
                        "Height: " + jObject.getString("height") + "\n" +
                        "Weight: " + jObject.getString("weight");

            // Save curID JSON
            curPokeId = jObject.getString("id");

            MainActivity mainActivity = mainActivityWeakReference.get();
            mainActivity.curPoke = Integer.parseInt(curPokeId);

            // Get img SVG
            JSONObject sprites = new JSONObject(jObject.getString("sprites"));
            JSONObject other = new JSONObject(sprites.getString("other"));
            JSONObject dream_world = new JSONObject(other.getString("dream_world"));
            img  = dream_world.getString("front_default");

            // Get type/types
            JSONArray types = new JSONArray(jObject.getString("types"));
            for(int i=0; i<types.length(); i++){
                JSONObject type = new JSONObject(types.getString(i));
                JSONObject type2  = new JSONObject(type.getString("type"));
                strTypes.add(type2.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Set info
        MainActivity.txtDisplay.setText(this.results);


        // Set main img
        SvgLoader.pluck()
                .with(MainActivity.act)
                .load(img, MainActivity.imgPok);

        //IF TYPE IS ONLY ONE
        if(strTypes.size() < 2){
            MainActivity.imgType[1].setImageResource(android.R.color.transparent);
        }

        // Set img types
        for(int i=0; i<strTypes.size(); i++){
            MainActivity.imgType[i].setImageResource(MainActivity.act.getResources().getIdentifier(strTypes.get(i), "drawable", MainActivity.act.getPackageName()));
        }
        
    }
}

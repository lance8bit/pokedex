package com.example.pokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    public static Activity act;
    public static TextView txtDisplay;
    public static ImageView imgPok;
    public static Button btLeft, btRight;
    public static ImageButton btSearch, btTypes;
    //public String inicial = "1";
    public int curPoke = 1;
    public ArrayList<String> typesList = new ArrayList<String>();
    public ArrayList<String> pokeTypeSel = new ArrayList<String>();

    public static ImageView [] imgType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String inicial = "1";
        act = this;
        imgType = new ImageView[2];
        curPoke = 1;

        txtDisplay = findViewById(R.id.txtDisplay);
        imgPok = findViewById(R.id.imgPok);
        imgType[0] = findViewById(R.id.imgType0);
        imgType[1] = findViewById(R.id.imgType1);
        btLeft = findViewById(R.id.btnLeft);
        btRight = findViewById(R.id.btnRight);
        btSearch = findViewById(R.id.btnSearch);
        btTypes = findViewById(R.id.btnTypes);

        fetchData process_incial = new fetchData(inicial, this);
        process_incial.execute();

        fetchDataType fetchDataType = new fetchDataType(this);
        fetchDataType.execute();

        //Log.i("TYPES", "onCreate: "+typesList.get(1));

        btSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTxtSearch();
            }
        });

        btTypes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showTypeList();
            }
        });

        btLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int minus = curPoke;
                if(minus != 1){
                    minus = minus - 1;

                    fetchData process_minus = new fetchData(String.valueOf(minus), MainActivity.this);
                    process_minus.execute();
                }
            }
        });

        btRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pluse = curPoke;
                    pluse = pluse + 1;

                    fetchData process_pluse = new fetchData(Integer.toString(pluse), MainActivity.this);
                    process_pluse.execute();
            }
        });

    }

    public void showTxtSearch(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search a Pokemon");

        final EditText input = new EditText(this);
        input.setHint("Pokemon");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String pokSearch = input.getText().toString().toLowerCase();
                fetchData process = new fetchData(pokSearch, MainActivity.this);
                process.execute();

                Log.i("CURRENTPOKEID", "POKEid: " +curPoke);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void showTypeList(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a type")
                .setItems(typesList.toArray(new String[0]), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("SELECTED", "onClick: "+typesList.get(i));
                fetchPokesTypes getPokesList = new fetchPokesTypes(typesList.get(i), MainActivity.this);
                getPokesList.execute();
            }
        });
        builder.show();
    }
}
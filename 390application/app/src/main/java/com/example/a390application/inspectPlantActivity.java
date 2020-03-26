package com.example.a390application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class inspectPlantActivity extends AppCompatActivity {

    protected TextView plantsTitle;
    protected TextView plantsType;
    protected TextView plantsInfo;
    protected ListView plantsListView;
    protected FloatingActionButton moreDetailsButton;
    protected Button deletePlantButton;
    protected Bundle bundle = new Bundle();
    protected long givenID;
    protected double plantsIdealMoisture;
    protected String plantInfo;
    protected String plantType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);


        //The plant picked in the list
        Intent intent = getIntent();
        givenID = intent.getLongExtra("plantID", 0);

        Plant givenPlant = null;

        InsertPlant dbInsertPlant = new InsertPlant(this);

        List<Plant> plants = dbInsertPlant.getAllPlants();
        for(int i = 0; i < plants.size(); i++){
            if(givenID == plants.get(i).getID()){
                givenPlant = plants.get(i);
                break;
            }
        }


        plantsTitle = findViewById(R.id.plantName);
        plantsType = findViewById(R.id.plantType);
        plantsInfo  = findViewById(R.id.plantInfo);


        plantsTitle.setText(givenPlant.getName());
        plantsType.setText(givenPlant.getType());

        plantType = plantsType.getText().toString();

        plantsIdealMoisture = fetchIdealMoisture(plantType);

        plantInfo = /* "General Information: \n" + fetchInfo(plantsType.getText().toString()) + */
                "\nIdeal Moisture Level: " + plantsIdealMoisture /* + /"Ideal Light Level: " +
                fetchIdealLightingLevel(plantsType.getText().toString())*/;

        plantsInfo.setText(plantInfo);


        bundle.putLong("data", givenPlant.getID());

        moreDetailsButton = findViewById(R.id.moreDetailsFragment);
        deletePlantButton = findViewById(R.id.deletePlant);


        moreDetailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MoreDetailsDialogFragment dialog = new MoreDetailsDialogFragment();

                dialog.setArguments(bundle);

                //////////
                dialog.show(getSupportFragmentManager(), "MoreDetailsFragment");
                /////////
            }
        });


        //'Delete Plant' button not yet implemented
        deletePlantButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //https://developer.android.com/training/data-storage/sqlite check here

            }
        });

    }


    //'Delete Plant' button not yet implemented.
    public void removePlantByID(long id){

    }


    @Override
    protected void onStart() {
        super.onStart();
        //loadPlantListView(givenID);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public double fetchIdealMoisture(String type) {
        Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
        double idealMoisture = -1;
        if (type.equals("Devil's Ivy"))
            idealMoisture = -1;
        else if (type.equals("English Ivy"))
            idealMoisture = -1;
        else if (type.equals("Sansevieria"))
            idealMoisture = 100;

        String ideal;
        ideal = Double.toString(idealMoisture);
        Toast.makeText(getApplicationContext(),ideal,Toast.LENGTH_SHORT).show();
        return idealMoisture;

    }
/*
    public double fetchIdealLightingLevel(String type) {
        double idealMoisture;
        if (type == "Cactus")
            idealMoisture = 50;
        else
        if (type == "Aloe" )
            idealMoisture = 75;
        else
        if (type == "Sansevieria")
            idealMoisture = 100;
        else
            idealMoisture = 0;
        return idealMoisture;
    }*/
}
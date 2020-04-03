package com.example.a390application;

import android.app.Notification;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class inspectPlantActivity extends AppCompatActivity {

    protected TextView plantsTitle;
    protected TextView plantsType;
    protected TextView plantsInfo;
    protected ImageView plantImage;
    protected Button deletePlantButton;
    protected Bundle bundle = new Bundle();
    protected long givenID;
    protected String plantsIdealMoisture;
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
        plantImage = findViewById(R.id.plantImage);


        plantsTitle.setText(givenPlant.getName());
        plantsType.setText(givenPlant.getType());

        plantType = plantsType.getText().toString();

        switch (plantType) {
            case "Devil's Ivy": {
                plantImage.setImageResource(R.drawable.devilivy);
                break;
            }
            case "Sansevieria": {
                plantImage.setImageResource(R.drawable.sanseivieria);
                break;
            }
            case "English Ivy": {
                plantImage.setImageResource(R.drawable.englishivy);
                break;
            }
            default:
                break;
        }

        plantsIdealMoisture = fetchIdealMoisture(plantType);

        plantInfo =
                "\nMoisture Level: " + plantsIdealMoisture + "\nLight Level: " +
                fetchIdealLightingLevel(plantType);

        plantsInfo.setText(plantInfo);


        bundle.putLong("data", givenPlant.getID());


        deletePlantButton = findViewById(R.id.deletePlant);





        //'Delete Plant' button not yet implemented
        deletePlantButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //https://developer.android.com/training/data-storage/sqlite check here

            }
        });

    }


    //'Delete Plant' button not yet implemented.
    /*public void removePlantByID(long id){

    }*/


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

    public String fetchIdealMoisture(String type) {
        Toast.makeText(getApplicationContext(),type,Toast.LENGTH_SHORT).show();
        String idealMoisture = "Not Defined";
        switch (type) {
            case "Devil's Ivy":
                idealMoisture = "Dry to Moderate";
                break;
            case "English Ivy":
                idealMoisture = "Wet to Moderate";
                break;
            case "Sansevieria":
                idealMoisture = "Drought-tolerant";
                break;
        }
        return idealMoisture;

    }

    public String fetchIdealLightingLevel(String type) {
        String idealLight = "Not Defined";
        switch (type) {
            case "Devil's Ivy":
                idealLight = "Partial to Full Shade";
                break;
            case "English Ivy":
                idealLight  = "Full Sun to Partial Shade";
                break;
            case "Sansevieria":
                idealLight  = "Partial Shade";
                break;
        }
        return idealLight;
    }
}
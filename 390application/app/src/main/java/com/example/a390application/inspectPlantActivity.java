package com.example.a390application;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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
    protected TextView plantsInfo2;
    protected ImageView plantImage;
    protected FloatingActionButton takeImageFloatingButton;
    protected Button deletePlantButton;
    protected Bundle bundle = new Bundle();
    protected long givenID;
    protected String plantInfo;
    protected String plantType;
    protected Plant givenPlant;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);


        //The plant picked in the list
        Intent intent = getIntent();
        givenID = intent.getLongExtra("plantID", 0);

        givenPlant = null;

        InsertPlant dbInsertPlant = new InsertPlant(this);

        List<Plant> plants = dbInsertPlant.getAllPlants();
        for (int i = 0; i < plants.size(); i++) {
            if (givenID == plants.get(i).getID()) {
                givenPlant = plants.get(i);
                break;
            }
        }


        plantsTitle = findViewById(R.id.plantName);
        plantsType = findViewById(R.id.plantType);
        plantsInfo = findViewById(R.id.plantInfo);
        plantsInfo2 = findViewById(R.id.plantInfo2);
        plantImage = findViewById(R.id.plantImage);



        plantsTitle.setText(givenPlant.getName());
        plantsType.setText(givenPlant.getType());


        plantType = plantsType.getText().toString();

        switch (plantType) {
            case "Devil's Ivy": {
                Bitmap bitmap = new ImageSaver(this).
                        setFileName(givenPlant.getName() +".jpg").
                        setDirectory("dir_name").
                        load();
                if (bitmap == null)
                    plantImage.setImageResource(R.drawable.devilivy);
                else
                    plantImage.setImageBitmap(bitmap);
                break;


            }
            case "Sansevieria": {
                Bitmap bitmap = new ImageSaver(this).
                        setFileName(givenPlant.getName() +".jpg").
                        setDirectory("dir_name").
                        load();
                if (bitmap == null)
                    plantImage.setImageResource(R.drawable.sanseivieria);
                else
                    plantImage.setImageBitmap(bitmap);
                break;
            }
            case "English Ivy": {
                Bitmap bitmap = new ImageSaver(this).
                        setFileName(givenPlant.getName() +".jpg").
                        setDirectory("dir_name").
                        load();
                if (bitmap == null)
                    plantImage.setImageResource(R.drawable.englishivy);
                else
                    plantImage.setImageBitmap(bitmap);
                break;
            }
            case "Spider": {
                Bitmap bitmap = new ImageSaver(this).
                        setFileName(givenPlant.getName() +".jpg").
                        setDirectory("dir_name").
                        load();
                if (bitmap == null)
                    plantImage.setImageResource(R.drawable.spider);
                else
                    plantImage.setImageBitmap(bitmap);
                break;
            }
            default:
                break;
        }

        switch (plantType) {
            case "Devil's Ivy": {
                plantsInfo2.setText("Hi! I am " + givenPlant.getName() + ". I love to grow and grow and grow -" +
                        " but I am also relatively low-maintenance, so I won’t eat up too much space in your diary. " +
                        "Simply pop me in a spot that receives indirect sunlight (although I will do just fine by a shady " +
                        "window), water me when the soil’s dry and voila! Did I mention my trailing vines are pros at " +
                        "removing pollutants like benzene, trichloroethylene, xylene and formaldehyde from the air, too?");
                break;
            }
            case "Sansevieria": {
                plantsInfo2.setText("Hi! I am " + givenPlant.getName() + ". I am virtually indestructible. " +
                        "I grow in any light level, including those shadowy corners indoors just begging for some " +
                        "greenery. There’s just one tricky aspect to taking care of me, and that’s knowing how often " +
                        "to water me. The short answer is to water me only when the soil is almost thoroughly dry. " +
                        "Even then, it won’t hurt to wait another few days, especially if you overwatered me by accident.");
                break;
            }
            case "English Ivy": {
                plantsInfo2.setText("Hi! I am " + givenPlant.getName() + ". I am a superb climber, "
                        + "clinging to almost any surface by means of small roots that grow along my stems. "
                        + "Taking care of me is a snap, so you can plant me in distant and hard-to-reach areas "
                        + "without worrying about my maintenance.");
                break;
            }
            case "Spider": {
                plantsInfo2.setText("Hi! I am " + givenPlant.getName() + ". I am an easy-to-grow houseplants that " +
                        "looks especially nice in a hanging basket. During the summer, I produce tiny white flowers " +
                        "on long stems, as well as babies called “pups.” The pups look like tiny spiders, hence my name!" +
                        " I was once highlighted by NASA for my air-purifying ability. There you go! We have now been introduced.");
            }
            default:
                break;
        }

        plantInfo =
                "Moisture: " + fetchIdealMoisture(plantType) + "\nLight: " +
                        fetchIdealLightingLevel(plantType) + "\nTemperature: "
                        + fetchIdealTemperature(plantType) + "\nHumidity: " + fetchIdealHumidity(plantType);

        plantsInfo.setText(plantInfo);


        bundle.putLong("data", givenPlant.getID());


        deletePlantButton = findViewById(R.id.deletePlant);
        takeImageFloatingButton = findViewById(R.id.takeImage);


        takeImageFloatingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "I want image", Toast.LENGTH_SHORT).show();
                dispatchTakePictureIntent();



            }
        });




        //'Delete Plant' button not yet implemented
        /*deletePlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://developer.android.com/training/data-storage/sqlite check here
            }
        });*/

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
            case "Spider":
                idealMoisture = "Moderate";
                break;
        }
        return idealMoisture;

    }

    public String fetchIdealHumidity(String type) {
        String idealHumidty = "Not Defined";
        switch (type) {
            case "Devil's Ivy":
                idealHumidty = "High";
                break;
            case "English Ivy":
                idealHumidty = "Average to High";
                break;
            case "Sansevieria":
                idealHumidty = "Average";
                break;
            case "Spider":
                idealHumidty = "Average";
                break;
        }
        return idealHumidty;
    }

    public String fetchIdealLightingLevel(String type) {
        String idealLight = "Not Defined";
        switch (type) {
            case "Devil's Ivy":
                idealLight = "Partial to Full Shade";
                break;
            case "English Ivy":
                idealLight = "Full Sun to Partial Shade";
                break;
            case "Sansevieria":
                idealLight = "Partial Shade";
                break;
            case "Spider":
                idealLight = "Partial Shade";
                break;
        }
        return idealLight;
    }

    public String fetchIdealTemperature(String type) {
        String idealTemperature = "Not Defined";
        switch (type) {
            case "Devil's Ivy":
                idealTemperature = "15-29 C";
                break;
            case "English Ivy":
                idealTemperature = "15-18 C";
                break;
            case "Sansevieria":
                idealTemperature = "21-32 C ";
                break;
            case "Spider":
                idealTemperature = "13-27 C";
                break;
        }
        return idealTemperature;
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            plantImage.setImageBitmap(imageBitmap);
            new ImageSaver(this)
                    .setFileName(givenPlant.getName() +".jpg")
                    .setExternal(false)//image save in external directory or app folder default value is false
                    .setDirectory("dir_name")
                    .save(imageBitmap); //Bitmap from your code
        }
    }

}
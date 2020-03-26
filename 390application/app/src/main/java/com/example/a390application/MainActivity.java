package com.example.a390application;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TextView plantNumber;
    protected ListView plantsListView;
    protected FloatingActionButton addPlantFloatingButton;
    protected List<Plant> plants;
    protected int plantCount;
    protected InsertPlant dbInsertPlant = new InsertPlant(this );
    protected DatabaseReference userReference;
    protected String uniqueID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        uniqueID = dbInsertPlant.checkUID();
        Toast.makeText(getApplicationContext(), uniqueID, Toast.LENGTH_SHORT).show();

        new DatabaseHelper(uniqueID).readPlants(new DatabaseHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Plant> plantData) {
                findViewById(R.id.loading).setVisibility(View.VISIBLE);


                for(int i = 0; i < plantData.size(); i++){

                    //FOR TESTING
                    //String checkData = plantData.get(i).getName() + " " + plantData.get(i).getType() + " " + plantData.get(i).getMoisture() + " " + plantData.get(i).getPh();
                    //Toast.makeText(getApplicationContext(), checkData, Toast.LENGTH_SHORT).show();

                    dbInsertPlant.modifyPlant(plantData.get(i));
                }

                loadListView();
                findViewById(R.id.loading).setVisibility(View.GONE);
            }
        });

        plantNumber = findViewById(R.id.plantName);
        plantsListView = findViewById(R.id.plantsListView);


        addPlantFloatingButton = findViewById(R.id.addPlantFloatingButton);

        loadListView();

        addPlantFloatingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                InsertPlantDialogFragment dialog = new InsertPlantDialogFragment();
                dialog.ownerID = uniqueID;

                dialog.show(getSupportFragmentManager(), "InsertPlantFragment");
            }
        });

        plantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                goToChild(plants.get(position).getID());
            }
        });
    }

    protected void goToChild(long id){
        Intent intent = new Intent(this, inspectPlantActivity.class);

        intent.putExtra("plantID", id);
        startActivity(intent);
    }

    protected void loadListView(){

        plants = dbInsertPlant.getAllPlants();

        plantCount = 0;

        ArrayList<String> plantTitles = new ArrayList<>();
        ArrayList<String> plantData = new ArrayList<>();
        ArrayList<Integer> plantImages = new ArrayList<>();

        for(int i = 0; i < plants.size(); i++){

            plantCount++;
            String tempTitle = "";
            String tempData = "";
            Integer tempImages = R.drawable.notfound;

            tempTitle += plants.get(i).getName();
            tempData+= "Type: ";
            tempData+= plants.get(i).getType() + "\n";
            tempData+= "Moisture: ";
            tempData+= plants.get(i).getMoisture() + "\n";
            tempData+= "Light Intensity: ";
            tempData+= plants.get(i).getLightIntensity() + "\n";
            tempData+= "Temperature: ";
            tempData+= plants.get(i).getTemperature() + "\n";
            tempData+= "Ph: ";
            tempData+= plants.get(i).getPH() + "\n";
            tempData+= "Test: ";
            tempData+= plants.get(i).getTest() + "\n";

            tempData+= "Health Percentage: ";
			if(plants.get(i).getType().equals("Devil's Ivy")){
			    double percentage = healthBarAlgoDevilsIvy(plants.get(i));
                tempData+= percentage + "%\n";

                tempImages = R.drawable.devilivy;

                //Notification sent when health reaches less than 50%.
                if(percentage < 50){
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.alert)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sanseivieria))
                            .setContentTitle(plants.get(i).getName() + " needs your attention!")
                            .setContentText("Your plant's health is lower than 50%!");
                    notificationBuilder.setDefaults(
                            Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                    notificationManager.notify(1, notificationBuilder.build());
                }
			}
			else if(plants.get(i).getType().equals("Sansevieria")){
                double percentage = healthBarAlgoSansevieria(plants.get(i));
                tempData+= percentage + "%\n";

                tempImages = R.drawable.sanseivieria;

                //Notification sent when health reaches less than 50%.
                if(percentage < 50){
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.alert)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sanseivieria))
                            .setContentTitle(plants.get(i).getName() + " needs your attention!")
                            .setContentText("Your plant's health is lower than 50%!");
                    notificationBuilder.setDefaults(
                            Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                    notificationManager.notify(1, notificationBuilder.build());
                }
			}
			else if(plants.get(i).getType().equals("English Ivy")){
                double percentage = healthBarAlgoEnglishIvy(plants.get(i));
                tempData+= percentage + "%\n";

                tempImages = R.drawable.englishivy;

                //Notification sent when health reaches less than 50%.
                if(percentage < 50){
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.alert)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sanseivieria))
                            .setContentTitle(plants.get(i).getName() + " needs your attention!")
                            .setContentText("Your plant's health is lower than 50%!");
                    notificationBuilder.setDefaults(
                            Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                    notificationManager.notify(1, notificationBuilder.build());
                }
			}
			else{
			}

            plantTitles.add(tempTitle);
            plantImages.add(tempImages);
            plantData.add(tempData);
        }

        if(plantCount == 1){
            plantNumber.setText(plantCount + " plant is being monitored.");
        }
        else{
            plantNumber.setText(plantCount + " plants are being monitored.");
        }


        CustomListView customListView = new CustomListView(this,plantTitles,plantData,plantImages);

        plantsListView.setAdapter(customListView);
    }

    protected double healthBarAlgoSansevieria(Plant givenPlant){       //specifically for Sansevieria

        double FinalTemperaturePercentage = 0;
        double FinalMoisturePercentage = 0;
        double LightIntensityValue = 0;


        // Do all calculations and store in 'FinalPercentage'.
        //givenPlant.getTemperature()' accesses temperature.

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }

        if (givenPlant.getMoisture() >= 767){
            FinalMoisturePercentage = 100-((givenPlant.getMoisture()-767)*0.1953);          // 1023 pts = 50%
        }                                                                                   // 767 pts = 100%
        else{
            FinalMoisturePercentage = givenPlant.getMoisture() * 0.1304;                    // 0 pts = 0 %
        }

        // calculation final
        //double FinalPercentage = 0.33*FinalMoisturePercentage + 0.33*FinalLightIntensityPercentage + 0.33*FinalTemperaturePercentage;   // weighting , all equal 1/3
        double FinalPercentage = 0.5*FinalMoisturePercentage + 0.5*FinalTemperaturePercentage;
        return FinalPercentage;
    }

    protected double healthBarAlgoEnglishIvy(Plant givenPlant){       //specifically for English Ivy/Ivy String

        double FinalTemperaturePercentage = 0;
        double FinalMoisturePercentage = 0;
        double LightIntensityValue = 0;


        //Do all calculations and store in 'FinalPercentage'.
        //givenPlant.getTemperature()' accesses temperature.

        if (givenPlant.getTemperature() >= 20){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-20)*3.75);       // 40deg C = 25%
        }                                                                                   // 20 deg C = 100%
        else{
            FinalTemperaturePercentage = 50+(givenPlant.getTemperature() * 2.5);            // -20deg C = 0%
        }

        if (givenPlant.getMoisture() >= 256){
            FinalMoisturePercentage = Math.abs(100-((givenPlant.getMoisture()-256)*0.1304));     // 1023 pts (Dry) = 0%
        }                                                                                   // 256 pts = 100%
        else{
            FinalMoisturePercentage = 50 + (givenPlant.getMoisture() * 0.1953);             // 0 pts (wet)  = 50%
        }

        // calculation final
        //double FinalPercentage = 0.33*FinalMoisturePercentage + 0.33*FinalLightIntensityPercentage + 0.33*FinalTemperaturePercentage;   // weighting , all equal 1/3
        double FinalPercentage = 0.5*FinalMoisturePercentage + 0.5*FinalTemperaturePercentage;
        return FinalPercentage;
    }

    protected double healthBarAlgoDevilsIvy(Plant givenPlant){       //specifically for Devil's Ivy

        double FinalTemperaturePercentage = 0;
        double FinalMoisturePercentage = 0;
        double LightIntensityValue = 0;

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }


        if (givenPlant.getMoisture() >= 512){
            FinalMoisturePercentage = 100-((givenPlant.getMoisture()-512)*0.1953);      // 1023 pts (Dry) = 0%
        }                                                                               // 512 pts = 100%
        else{
            FinalMoisturePercentage = givenPlant.getMoisture() * 0.1953;                // 0 pts (wet) = 0%
        }

        // calculation final
        //double FinalPercentage = 0.33*FinalMoisturePercentage + 0.33*FinalLightIntensityPercentage + 0.33*FinalTemperaturePercentage;   // weighting , all equal 1/3
        double FinalPercentage = 0.5*FinalMoisturePercentage + 0.5*FinalTemperaturePercentage;
        return FinalPercentage;
    }


    @Override
    protected void onStart() {
        super.onStart();

        loadListView();
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
}



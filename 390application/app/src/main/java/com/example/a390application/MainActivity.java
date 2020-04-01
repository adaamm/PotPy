package com.example.a390application;

import android.app.Notification;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TextView plantNumber;
    protected ListView plantsListView;
    protected FloatingActionButton addPlantFloatingButton;
    protected List<Plant> plants;
    protected int plantCount;
    protected InsertPlant dbInsertPlant = new InsertPlant(this );
    protected String uniqueID;

    /*
    MAKE A SLOT IN FIREBASE WHERE THE USER WILL SPECIFY WHICH PLANT HE/SHE WANTS TO MONITOR.
     */

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
            int tempImages = R.drawable.notfound;

            tempTitle += plants.get(i).getName();
            tempData+= "Type: ";
            tempData+= plants.get(i).getType() + "\n";
            tempData+= "Moisture: ";
            if(plants.get(i).getMoisture() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                tempData+= String.format("%.1f",plants.get(i).getMoisture()) + "\n";
            }

            tempData+= "Light Intensity: ";
            if(plants.get(i).getLightIntensity() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                tempData+= String.format("%.1f",plants.get(i).getLightIntensity()) + "\n";
            }

            tempData+= "Temperature: ";
            if(plants.get(i).getTemperature() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                tempData+= String.format("%.1f",plants.get(i).getTemperature()) + "\n";
            }
            tempData+= "Humidity: ";

            if(plants.get(i).getHumidity() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                tempData+= String.format("%.1f",plants.get(i).getHumidity()) + "\n";
            }
            //tempData+= "Test: ";
            //tempData+= plants.get(i).getTest() + "\n";

            tempData+= "Health Percentage: ";
            switch (plants.get(i).getType()) {
                case "Devil's Ivy": {
                    double percentage = healthBarAlgoDevilsIvy(plants.get(i));
                    if(percentage < 0){
                        percentage = 0;
                    }
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.devilivy;

                    //Notification sent when health reaches less than 50%.
                    if (percentage < 50) {
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
                    break;
                }
                case "Sansevieria": {
                    double percentage = healthBarAlgoSansevieria(plants.get(i));
                    if(percentage < 0){
                        percentage = 0;
                    }
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.sanseivieria;

                    //Notification sent when health reaches less than 50%.
                    if (percentage < 50) {
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
                    break;
                }
                case "English Ivy": {
                    double percentage = healthBarAlgoEnglishIvy(plants.get(i));
                    if(percentage < 0){
                        percentage = 0;
                    }
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.englishivy;

                    //Notification sent when health reaches less than 50%.
                    if (percentage < 50) {
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
                    break;
                }
                default:
                    break;
            }

            plantTitles.add(tempTitle);
            plantImages.add(tempImages);
            plantData.add(tempData);
        }

        if(plantCount == 1){
            String temp = plantCount + " plant is being monitored.";
            plantNumber.setText(temp);
        }
        else{
            String temp = plantCount + " plants are being monitored.";
            plantNumber.setText(temp);
        }


        CustomListView customListView = new CustomListView(this,plantTitles,plantData,plantImages);

        plantsListView.setAdapter(customListView);
    }

    protected double healthBarAlgoSansevieria(Plant givenPlant){       //specifically for Sansevieria

        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
        //double LightIntensityValue = 0;


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
        return 0.5*FinalMoisturePercentage + 0.5*FinalTemperaturePercentage;
    }

    protected double healthBarAlgoEnglishIvy(Plant givenPlant){       //specifically for English Ivy/Ivy String

        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
        //double LightIntensityValue = 0;


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
        return 0.5*FinalMoisturePercentage + 0.5*FinalTemperaturePercentage;
    }

    protected double healthBarAlgoDevilsIvy(Plant givenPlant){       //specifically for Devil's Ivy

        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
        //double LightIntensityValue = 0;

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
        return 0.5*FinalMoisturePercentage + 0.5*FinalTemperaturePercentage;
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


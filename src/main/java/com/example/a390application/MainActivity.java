package com.example.a390application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a390application.InsertPlant.Config;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        //InsertPlant dbInsertPlant = new InsertPlant(this );
        plants = dbInsertPlant.getAllPlants();

        plantCount = 0;

        ArrayList<String> plantsListText = new ArrayList<>();

        for(int i = 0; i < plants.size(); i++){

            plantCount++;
            String temp = "";
            temp += plants.get(i).getName() + "\n \n";
            temp+= "Type: ";
            temp+= plants.get(i).getType() + "\n";
            temp+= "Moisture: ";
            temp+= plants.get(i).getMoisture() + "\n";
            temp+= "Light Intensity: ";
            temp+= plants.get(i).getLightIntensity() + "\n";
            temp+= "Temperature: ";
            temp+= plants.get(i).getTemperature() + "\n";
            temp+= "Ph: ";
            temp+= plants.get(i).getPH() + "\n";
            temp+= "Test: ";
            temp+= plants.get(i).getTest() + "\n";


            plantsListText.add(temp);
        }

        if(plantCount == 1){
            plantNumber.setText(plantCount + " plant is being monitored.");
        }
        else{
            plantNumber.setText(plantCount + " plants are being monitored.");
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, plantsListText);

        plantsListView.setAdapter(arrayAdapter);
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



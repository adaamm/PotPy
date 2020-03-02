package com.example.a390application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TextView plantNumber;
    protected ListView plantsListView;
    protected FloatingActionButton addPlantFloatingButton;
    protected List<Plant> plants;
    protected int plantCount;
    protected DatabaseReference databaseAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseAccess = FirebaseDatabase.getInstance().getReference().child("Plants");

        plantNumber = findViewById(R.id.plantName);
        plantsListView = findViewById(R.id.plantsListView);
        addPlantFloatingButton = findViewById(R.id.addPlantFloatingButton);

        loadListView();

        addPlantFloatingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                InsertPlantDialogFragment dialog = new InsertPlantDialogFragment();

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


        //Extract from database.
        databaseAccess.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                extractOnChange(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void goToChild(long id){
        Intent intent = new Intent(this, inspectPlantActivity.class);

        intent.putExtra("plantID", id);
        startActivity(intent);
    }

    protected void extractOnChange(DataSnapshot dataSnapshot){
        Toast.makeText(this, "Change in Database detected.", Toast.LENGTH_SHORT).show();
        for(DataSnapshot snap : dataSnapshot.getChildren()){
            InsertPlant dbInsertPlant = new InsertPlant(this);
//            plants = dbInsertPlant.getAllPlants();

//            for(int i = 0; i < plants.size(); i++){
//                if(snap.child(plants.get(i).getName()) != null){
                    Toast.makeText(this, "Tried to change plant data.", Toast.LENGTH_SHORT).show();
                    Plant currentPlant = new Plant(snap.getValue(Plant.class).getName(), snap.getValue(Plant.class).getType(), snap.getValue(Plant.class).getMoisture(), snap.getValue(Plant.class).getPh());
                    dbInsertPlant.updatePlant(currentPlant);
                    //plants.get(i).setMoisture(snap.child(plants.get(i).getName()).getValue(Plant.class).getMoisture());
                    //plants.get(i).setPh(snap.child(plants.get(i).getName()).getValue(Plant.class).getPh());
//                }
//            }
        }
    }

    protected void loadListView(){
        InsertPlant dbInsertPlant = new InsertPlant(this );
        plants = dbInsertPlant.getAllPlants();

        plantCount = 0;

        ArrayList<String> plantsListText = new ArrayList<>();

        for(int i = 0; i < plants.size(); i++){
            //DatabaseReference accesschild = FirebaseDatabase.getInstance().getReference().child(plants.get(i).getName());
            plantCount++;
            String temp = "";
            temp += plants.get(i).getName() + "\n \n";
            temp+= "Type: ";
            temp+= plants.get(i).getType() + "\n";
            temp+= "Moisture: ";

            //plants.get(i).setMoisture(Double.parseDouble(databaseAccess.getKey()));
            temp+= plants.get(i).getMoisture() + "\n";
            temp+= "Ph: ";
           // plants.get(i).setMoisture(Double.parseDouble(databaseAccess.getKey()));
            temp+= plants.get(i).getPh() + "\n";


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



package com.example.a390application;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.a390application.InsertPlant.Plant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

class DatabaseHelper {
    private DatabaseReference reference;
    private List<Plant> plantData = new ArrayList<>();
    private String ownerID;

    public interface DataStatus{
        void DataLoaded (List<Plant> plantData);
    }

    DatabaseHelper(String uniqueID) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        ownerID = uniqueID;
    }

    void readPlants(final DataStatus dataStatus){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantData.clear();
                // for(DataSnapshot root : dataSnapshot.getChildren()){
                // if((root.getKey()).equals("Users")) {
                for (DataSnapshot users : dataSnapshot.child("Users").getChildren()) {
                    if (ownerID.equals(users.getKey())) {
                        for (DataSnapshot eachPlantData : users.getChildren()) {
                            if(!eachPlantData.getKey().equals("ImageImageImage")){
                                String name = eachPlantData.child("name").getValue().toString();
                                String type = eachPlantData.child("type").getValue().toString();
                                double moisture = Double.parseDouble(eachPlantData.child("moisture").getValue().toString());
                                double lightIntensity = Double.parseDouble(eachPlantData.child("lightIntensity").getValue().toString());
                                String test = eachPlantData.child("test").getValue().toString();
                                double humidity = Double.parseDouble(eachPlantData.child("humidity").getValue().toString());
                                double temperature = Double.parseDouble(eachPlantData.child("temperature").getValue().toString());

                                plantData.add(new Plant(name, type, moisture, lightIntensity, test, humidity, temperature, ownerID));
                            }
                        }
                    }
                }
                dataStatus.DataLoaded(plantData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

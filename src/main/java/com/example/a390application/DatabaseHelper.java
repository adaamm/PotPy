package com.example.a390application;

import androidx.annotation.NonNull;

import com.example.a390application.InsertPlant.Plant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    protected FirebaseDatabase database;
    protected DatabaseReference reference;
    protected List<Plant> plantData = new ArrayList<>();
    protected String ownerID;

    public interface DataStatus{
        void DataLoaded (List<Plant> plantData);
    }

    public DatabaseHelper(String uniqueID) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        ownerID = uniqueID;
    }

    public void readPlants(final DataStatus dataStatus){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantData.clear();
                for(DataSnapshot root : dataSnapshot.getChildren()){
                    for(DataSnapshot users: root.getChildren()){
                        for(DataSnapshot eachPlantData: users.getChildren()){
                            if(ownerID.equals(eachPlantData.child("ownerID").getValue().toString())){
                                String name = eachPlantData.child("name").getValue().toString();
                                String type = eachPlantData.child("type").getValue().toString();
                                double moisture = Double.parseDouble(eachPlantData.child("moisture").getValue().toString());
                                double lightIntensity = Double.parseDouble(eachPlantData.child("lightIntensity").getValue().toString());
                                String test = eachPlantData.child("test").getValue().toString();
                                double ph = Double.parseDouble(eachPlantData.child("ph").getValue().toString());
                                double temperature = Double.parseDouble(eachPlantData.child("temperature").getValue().toString());

                                plantData.add(new Plant(name,type,moisture,lightIntensity,test,ph,temperature,ownerID));
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

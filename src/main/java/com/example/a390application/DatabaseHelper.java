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

    public interface DataStatus{
        void DataLoaded (List<Plant> plantData);
    }

    public DatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void readPlants(final DataStatus dataStatus){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantData.clear();
                for(DataSnapshot eachPlantData : dataSnapshot.getChildren()){
                    String name = eachPlantData.child("name").getValue().toString();
                    String type = eachPlantData.child("type").getValue().toString();
                    double moisture = Double.parseDouble(eachPlantData.child("moisture").getValue().toString());
                    double ph = Double.parseDouble(eachPlantData.child("ph").getValue().toString());

                    plantData.add(new Plant(name,type,moisture,ph));
                }
                dataStatus.DataLoaded(plantData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

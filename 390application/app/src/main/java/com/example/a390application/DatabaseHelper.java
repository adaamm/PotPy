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

/*
This class is used to:  - detect any changes in the plant data of the user's plants in Firebase.
                        - read the plant data of a user's plants from Firebase.
 */
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

    //This method is used to keep track of any changes in the data of the user's plants, the new data is read upon detection of a change.
    void readPlants(final DataStatus dataStatus){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plantData.clear();
                for (DataSnapshot users : dataSnapshot.child("Users").getChildren()) {
                    if (ownerID.equals(users.getKey())) {
                        for (DataSnapshot eachPlantData : users.getChildren()) {
                            String name = eachPlantData.child("name").getValue().toString();
                            String type = eachPlantData.child("type").getValue().toString();
                            double moisture = Double.parseDouble(eachPlantData.child("moisture").getValue().toString());
                            double lightIntensity = Double.parseDouble(eachPlantData.child("lightIntensity").getValue().toString());
                            double humidity = Double.parseDouble(eachPlantData.child("humidity").getValue().toString());
                            double temperature = Double.parseDouble(eachPlantData.child("temperature").getValue().toString());

                            plantData.add(new Plant(name, type, moisture, lightIntensity, humidity, temperature, ownerID));
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

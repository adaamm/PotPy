package com.example.a390application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class inspectPlantActivity extends AppCompatActivity {

    protected TextView plantsTitle;
    protected ListView plantsListView;
    protected FloatingActionButton moreDetailsButton;
    protected Button deletePlantButton;
    protected Bundle bundle = new Bundle();
    protected long givenID;



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
        plantsTitle.setText(givenPlant.getName());


        plantsListView = findViewById(R.id.plantsListView);
        //loadPlantListView(givenID);

        bundle.putLong("data", givenPlant.getID());

        moreDetailsButton = findViewById(R.id.moreDetailsFragment);
        deletePlantButton = findViewById(R.id.deletePlant);


        moreDetailsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MoreDetailsDialogFragment dialog = new MoreDetailsDialogFragment();

                dialog.setArguments(bundle);

                //////////
                dialog.show(getSupportFragmentManager(), "MoreDetailsFragment");
                /////////
            }
        });


        //'Delete Plant' button not yet implemented
        deletePlantButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //https://developer.android.com/training/data-storage/sqlite check here

            }
        });

    }


    //'Delete Plant' button not yet implemented.
    public void removePlantByID(long id){

    }


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
}



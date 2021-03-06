package com.example.a390application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;


public class InsertPlantDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private List<Plant> plants;
    private EditText plantNameEditText;
    private String typePicked;
    protected String ownerID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_plant, container, false);

        plantNameEditText = view.findViewById(R.id.plantNameEditText);
        Spinner plantTypeSpinner = view.findViewById(R.id.plantTypeDrop);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantTypeSpinner.setAdapter(adapter);

        plantTypeSpinner.setOnItemSelectedListener(this);


        Button addPlantButton = view.findViewById(R.id.addPlantButton);
        Button cancelPlantButton = view.findViewById(R.id.backButton);


        addPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = plantNameEditText.getText().toString();

                InsertPlant dbInsertPlant = new InsertPlant(getActivity());
                plants = dbInsertPlant.getAllPlants();

                //Firebase path limitation.
                if (!name.equals("") && !name.contains(".") && !name.contains("#") && !name.contains("$") && !name.contains("[") && !name.contains("]")) {
                    boolean isUnique = true;
                    for (int i = 0; i < plants.size(); i++) {
                        if (plants.get(i).getName().equals(name)) {
                            isUnique = false;
                        }
                    }

                    if (isUnique) {
                        InsertPlant dbPlants = new InsertPlant(getActivity());

                        storePlantInDatabase(new Plant(name, typePicked, -10000,-10000, -10000, -10000, ownerID));
                        dbPlants.insertPlant(new Plant(name, typePicked, ownerID));


                        ((MainActivity) getActivity()).loadListView();
                        getDialog().dismiss();
                    } else {
                        Toast.makeText(getContext(), "Plant name should be unique!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Plant name must not be empty nor contain any of '.#$[]'", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        typePicked = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void storePlantInDatabase(Plant givenPlant) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(ownerID).child(givenPlant.getName());
        userReference.setValue(givenPlant);
    }

}

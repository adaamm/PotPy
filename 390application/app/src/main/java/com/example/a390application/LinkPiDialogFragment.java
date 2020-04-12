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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.PI;
import com.example.a390application.InsertPlant.Plant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class LinkPiDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private List<Plant> plants;
    private EditText PiIdEditText;
    private EditText PiPwEditText;
    private String plantPicked;
    protected String ownerID;
    protected ArrayList<String> currentPlants = new ArrayList<>();
    private TextView note;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_link_pi, container, false);

        PiIdEditText = view.findViewById(R.id.PiIdEditText);
        PiPwEditText = view.findViewById(R.id.PwEditText);
        Spinner plantOptionsSpinner = view.findViewById(R.id.plantOptions);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currentPlants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        plantOptionsSpinner.setAdapter(adapter);

        plantOptionsSpinner.setOnItemSelectedListener(this);

        note = view.findViewById(R.id.note);
        note.setText("Please Note: \nIf all the sensors of the Raspberry PI you are linking are already in use, the oldest link will be removed and will be replaced by this new link.");

        Button linkButton = view.findViewById(R.id.linkButton);
        Button cancelPlantButton = view.findViewById(R.id.cancelButton);

        linkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {



                final String PiId = PiIdEditText.getText().toString();
                final String PiPassword = PiPwEditText.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                if(!PiPassword.equals("") && !PiPassword.contains(".") && !PiPassword.contains("#") && !PiPassword.contains("$") && !PiPassword.contains("[") && !PiPassword.contains("]") && !PiId.equals("") && !PiId.contains(".") && !PiId.contains("#") && !PiId.contains("$") && !PiId.contains("[") && !PiId.contains("]")) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                checkPiPassword(PiId, dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), PiPassword);
                                //Toast.makeText(getContext(), dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                //Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
                                Toast.makeText(getContext(), "There is no PI with that Id!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Pi Id & Password must not be empty nor contain any of '.#$[]'", Toast.LENGTH_SHORT).show();
                }

                getDialog().dismiss();
            }
        });

        cancelPlantButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        plantPicked = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void checkPiPassword(String PiId, String PiPassword, String givenPassword) {
        if (PiPassword.equals(givenPassword)) {
            InsertPlant dbInsertPlant = new InsertPlant(getActivity());
            plants = dbInsertPlant.getAllPlants();

            for (int i = 0; i < plants.size(); i++) {
                if (plants.get(i).getName().equals(plantPicked)) {
                    linkPlant(plants.get(i), PiId,PiPassword);
                    break;
                }
            }
        } else {
            Toast.makeText(getContext(), "Password is incorrect!", Toast.LENGTH_SHORT).show();
        }
    }


    private void linkPlant(Plant givenPlant, String PiId, String password) {
        DatabaseReference PiReference = FirebaseDatabase.getInstance().getReference().child("PIs").child(PiId);
        PiReference.setValue(new PI(givenPlant.getName(),givenPlant.getOwnerID(),password));
    }
}

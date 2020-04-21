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

//This dialog fragment is used to direct the Raspberry Pi to send its data to a plant on the app which the user specifies.
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
        note.setText("Please Note: \n\n-If the Potpy reaches its maximum plant monitoring capacity then the oldest plant won't be monitored anymore and will be replaced by this new one.\n\n-Adding a plant that's already being monitored won't make a change.");

        Button linkButton = view.findViewById(R.id.linkButton);
        Button cancelPlantButton = view.findViewById(R.id.cancelButton);

        linkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String PiId = PiIdEditText.getText().toString();
                final String PiPassword = PiPwEditText.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                //These characters can't be used in Firebase.
                if(!PiPassword.equals("") && !PiPassword.contains(".") && !PiPassword.contains("#") && !PiPassword.contains("$") && !PiPassword.contains("[") && !PiPassword.contains("]") && !PiId.equals("") && !PiId.contains(".") && !PiId.contains("#") && !PiId.contains("$") && !PiId.contains("[") && !PiId.contains("]")) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                //Checks the validity of the data entered.
                                checkPiPassword(PiId, dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), PiPassword);
                            } catch (Exception e) {
                                //If an error is found due to not finding the Pi Id in Firebase, then that Pi Id doesn't exist there and the user is notified.
                                Toast.makeText(getContext(), "There is no PI with that Id!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    //The user is notified that the Pi Id & password must not be empty nor contain these characters: .#$[]
                    Toast.makeText(getContext(), "Pi Id & Password must not be empty nor contain any of '.#$[]'", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Closes the dialog when 'cancel' is pressed.
        cancelPlantButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    //This method extracts the data of the plant picked in the Spinner (the dropdown).
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        plantPicked = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //This portion checks the validity of the data entered by the user and notifies the user of any problems with the data entered.
    private void checkPiPassword(String PiId, String PiPassword, String givenPassword) {
        if (PiPassword.equals(givenPassword)) {
            InsertPlant dbInsertPlant = new InsertPlant(getActivity());
            plants = dbInsertPlant.getAllPlants();

            for (int i = 0; i < plants.size(); i++) {
                if (plants.get(i).getName().equals(plantPicked)) {
                    linkPlant(plants.get(i), PiId,PiPassword);

                    //Closes the dialog upon finishing.
                    getDialog().dismiss();
                    break;
                }
            }
        } else {
            //If the entered password doesn't match the one associated with the Pi in Firebase, the user is notified.
            Toast.makeText(getContext(), "Password is incorrect!", Toast.LENGTH_SHORT).show();
        }
    }

    //This method is used to direct the Raspberry Pi to send its data to a plant on the app which the user specifies (given that the information provided by the user were valid).
    private void linkPlant(Plant givenPlant, String PiId, String password) {
        DatabaseReference PiReference = FirebaseDatabase.getInstance().getReference().child("PIs").child(PiId);
        PiReference.setValue(new PI(givenPlant.getName(),givenPlant.getOwnerID(),password,"true","N/A"));
    }
}

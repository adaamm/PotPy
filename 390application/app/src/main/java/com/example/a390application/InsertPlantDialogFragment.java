package com.example.a390application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class InsertPlantDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private List<Plant> plants;
    private EditText plantNameEditText;
    //private EditText PiIdEditText;
    private String typePicked;
    protected String ownerID;
    protected FloatingActionButton takeImages;

    static final int REQUEST_IMAGE_CAPTURE = 111;
    public static final int GET_FROM_GALLERY = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insert_plant, container, false);

        plantNameEditText = view.findViewById(R.id.plantNameEditText);
        //PiIdEditText = view.findViewById(R.id.PiIdEditText);
        Spinner plantTypeSpinner = view.findViewById(R.id.plantTypeDrop);
        //plantImage = view.findViewById(R.id.plantImage);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantTypeSpinner.setAdapter(adapter);

        plantTypeSpinner.setOnItemSelectedListener(this);


        Button addPlantButton = view.findViewById(R.id.addPlantButton);
        Button cancelPlantButton = view.findViewById(R.id.backButton);
        takeImages = view.findViewById(R.id.takeImage);

        takeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                //onLaunchCamera();

            }
        });

        addPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = plantNameEditText.getText().toString();
                //String PiId = PiIdEditText.getText().toString();

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

                        storePlantInDatabase(new Plant(name, typePicked, -10000, -10000, "default", -10000, -10000, ownerID));
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

// not using this anymore
    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri);

                //Since no plant has been added yet at this stage, naming the plant isn't needed right?
                new ImageSaver(getActivity())
                        .setFileName("givenPlant.jpg")
                        .setExternal(false)//image save in external directory or app folder default value is false
                        .setDirectory("dir_name")
                        .save(bitmap); //Bitmap from your code

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference().child("Users")
                        .child(ownerID)
                        //.child(givenPlant.getName())
                        .child("Image");
                ref.setValue(imageEncoded);
                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();


                // Log.d(TAG, String.valueOf(bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //plantImage.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);

            //Since no plant has been added yet at this stage, naming the plant isn't needed right?
            new ImageSaver(getActivity())
                    .setFileName("givenPlant.jpg")
                    .setExternal(false)//image save in external directory or app folder default value is false
                    .setDirectory("dir_name")
                    .save(imageBitmap); //Bitmap from your code
        }
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //plantImage.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);

            //Since no plant has been added yet at this stage, naming the plant isn't needed right?
            new ImageSaver(getActivity())
                    .setFileName("givenPlant.jpg")
                    .setExternal(false)//image save in external directory or app folder default value is false
                    .setDirectory("dir_name")
                    .save(imageBitmap); //Bitmap from your code
        }
    }*/


    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference().child("Users")
                .child(ownerID)
                //.child(givenPlant.getName())
                .child("Image");
        ref.setValue(imageEncoded);
    }

}

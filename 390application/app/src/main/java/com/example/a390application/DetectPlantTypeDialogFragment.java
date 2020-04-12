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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DetectPlantTypeDialogFragment extends DialogFragment {

    private EditText IdEditText;
    private EditText PwEditText;
    private String plantTypeReceived;
    protected String ownerID;
    private TextView responseAndExplanation;
    protected String givenPiId;


    protected FloatingActionButton sendImageButton;
    static final int REQUEST_IMAGE_CAPTURE = 111;
    public static final int GET_FROM_GALLERY = 3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detect_type, container, false);

        IdEditText = view.findViewById(R.id.AiPiId);
        PwEditText = view.findViewById(R.id.AiPiPw);

        responseAndExplanation = view.findViewById(R.id.ResponseAndExplanation);
        responseAndExplanation.setText("Type of given Plant: \n");

        Button cancelButton = view.findViewById(R.id.cancelBT);

        sendImageButton = view.findViewById(R.id.takeImageDialogFrag);

        sendImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String PiId = IdEditText.getText().toString();
                final String PiPassword = PwEditText.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                if(!PiPassword.equals("") && !PiPassword.contains(".") && !PiPassword.contains("#") && !PiPassword.contains("$") && !PiPassword.contains("[") && !PiPassword.contains("]") && !PiId.equals("") && !PiId.contains(".") && !PiId.contains("#") && !PiId.contains("$") && !PiId.contains("[") && !PiId.contains("]")) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                checkPiPassword(PiId, dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), PiPassword);
                                //Toast.makeText(getContext(), dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
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

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



        return view;
    }

    private void checkPiPassword(String PiId, String PiPassword, String givenPassword) {
        if (PiPassword.equals(givenPassword)) {
            givenPiId = PiId;
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            //onLaunchCamera();
        } else {
            Toast.makeText(getContext(), "Password is incorrect!", Toast.LENGTH_SHORT).show();
        }
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
                        .getReference().child("PIs")
                        .child(givenPiId)
                        .child("Image");
                ref.setValue(imageEncoded);
                Toast.makeText(getActivity(), "Done! Please wait for the response.", Toast.LENGTH_SHORT).show();

                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            plantTypeReceived = dataSnapshot.child("PIs").child(givenPiId).child("type").getValue().toString();

                            responseAndExplanation.setText("Type of given Plant: \n" + plantTypeReceived);

                            //Toast.makeText(getContext(), dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Response from Pi not back in time.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


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
                .getReference().child("PIs")
                .child(givenPiId)
                .child("Image");
        ref.setValue(imageEncoded);
    }
}

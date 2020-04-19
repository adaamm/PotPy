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

/*
-This dialog fragment is used to send a given photo of a plant to the Raspberry Pi that the user owns (hence why the Pi Id & Password is needed) in order for the AI in
the Raspberry Pi to detect and show the user the type of that plant.
-This feature is optional for the user. It is only used to inform the user of the plant's type in case the user doesn't know (since upon adding a plant to the app, the
plant's type needs to be known beforehand)
 */
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

        View view = inflater.inflate(R.layout.fragment_detect_type, container, false); // open the dialog fragment

       // Activity elements are defined
        IdEditText = view.findViewById(R.id.AiPiId);
        PwEditText = view.findViewById(R.id.AiPiPw);

        // Type of plant will be shown here
        responseAndExplanation = view.findViewById(R.id.ResponseAndExplanation);
        responseAndExplanation.setText("Type of given Plant: \n");

        Button cancelButton = view.findViewById(R.id.cancelBT);

        sendImageButton = view.findViewById(R.id.takeImageDialogFrag);

        // here the image will be fetched from gallery and sent to the firebase database as a string value encoded in base 64
        sendImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // information required to connect the app to the pi to use AI
                final String PiId = IdEditText.getText().toString();
                final String PiPassword = PwEditText.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                //This portion checks the validity of the data entered by the user and notifies the user of any problems concerning the data entered.
                if(!PiPassword.equals("") && !PiPassword.contains(".") && !PiPassword.contains("#") && !PiPassword.contains("$") && !PiPassword.contains("[") && !PiPassword.contains("]") && !PiId.equals("") && !PiId.contains(".") && !PiId.contains("#") && !PiId.contains("$") && !PiId.contains("[") && !PiId.contains("]")) {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                checkPiPassword(PiId, dataSnapshot.child("PIs").child(PiId).child("password").getValue().toString(), PiPassword);
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

        //Closes the dialog when 'cancel' is pressed.
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });



        return view;
    }

    //This portion checks the validity of the data entered by the user and notifies the user of any problems with the data entered.
    private void checkPiPassword(String PiId, String PiPassword, String givenPassword) {
        if (PiPassword.equals(givenPassword)) {
            givenPiId = PiId;
            startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        } else {
            Toast.makeText(getContext(), "Password is incorrect!", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), uri); //bitmap object where the image fetched will be stored is created

                //Since no plant has been added yet at this stage, naming the plant isn't needed.
                // Image saved on app for further use if needed
                new ImageSaver(getActivity())
                        .setFileName("givenPlant.jpg")
                        .setExternal(false)//image save in external directory or app folder default value is false
                        .setDirectory("dir_name")
                        .save(bitmap); //Bitmap from your code

                ByteArrayOutputStream baos = new ByteArrayOutputStream(); //byte stream created
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // image stored in "bitmap" is now compressed as a PNG and saved in "baos"
                String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT); // image in "baos" now encoded as a string in base 64
                // image in string format added to firebase database
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference().child("PIs")
                        .child(givenPiId)
                        .child("Image");
                ref.setValue(imageEncoded);
                // imgUpdate branch in firebase updated as false to let know to the pi that it has an image to process
                ref = FirebaseDatabase.getInstance()
                        .getReference().child("PIs")
                        .child(givenPiId)
                        .child("imgUpdate");
                ref.setValue("false");
                // type is given initially as N/A to see if there is a change
                ref = FirebaseDatabase.getInstance()
                        .getReference().child("PIs")
                        .child(givenPiId)
                        .child("type");
                ref.setValue("N/A");
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
                            // information about the plant type is fetched from firebase and stored here
                            plantTypeReceived = dataSnapshot.child("PIs").child(givenPiId).child("type").getValue().toString();

                            String text = "Type of given Plant: \n" + plantTypeReceived;
                            responseAndExplanation.setText(text);

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Response from Pi not back in time.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

// Use the following if you want to use the camera instead of the gallery.
        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            encodeBitmapAndSaveToFirebase(imageBitmap);

            //Since no plant has been added yet at this stage, naming the plant isn't needed.
            new ImageSaver(getActivity())
                    .setFileName("givenPlant.jpg")
                    .setExternal(false)//image save in external directory or app folder default value is false
                    .setDirectory("dir_name")
                    .save(imageBitmap); //Bitmap from your code
        }*/
    }

// This function can be used to encode any bitmap in base 64 and return the value as a string
    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //byte stream created
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);// image stored in "bitmap" is now compressed as a PNG and saved in "baos"
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);// image in "baos" now encoded as a string in base 64
        // image in string format added to firebase database
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference().child("PIs")
                .child(givenPiId)
                .child("Image");
        ref.setValue(imageEncoded);
    }
}

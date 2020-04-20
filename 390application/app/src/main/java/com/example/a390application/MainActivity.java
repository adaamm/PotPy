package com.example.a390application;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.a390application.InsertPlant.InsertPlant;
import com.example.a390application.InsertPlant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected TextView plantNumber;
    protected ListView plantsListView;
    protected FloatingActionButton addPlantFloatingButton;
    protected List<Plant> plants;
    protected int plantCount;
    protected InsertPlant dbInsertPlant = new InsertPlant(this );
    protected String uniqueID;
    private NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_1_ID);
    public static final String CHANNEL_1_ID =  "channel1";
    protected Button linkPiButton;
    protected FloatingActionButton findType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*USED TO CREATE A NEW PI ID IN FIREBASE*/
        //Plant plant = new Plant("DEFAULT","Spider","DEFAULT");
        //DatabaseReference PiReference = FirebaseDatabase.getInstance().getReference().child("PIs").child("123");
        //PiReference.setValue(new PI(plant.getName(),plant.getOwnerID(),"password"));

        //notificationManager = NotificationManagerCompat.from(this);

        findType = findViewById(R.id.takeImage);

        //Sends the user to the dialog fragment that will handle the request.
        findType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetectPlantTypeDialogFragment dialog = new DetectPlantTypeDialogFragment();
                dialog.ownerID = uniqueID;

                dialog.show(getSupportFragmentManager(), "DetectTypeDialogFragment");

            }
        });

        uniqueID = dbInsertPlant.checkUID();

        //Here, the app will update the plant's readings on the app whenever a change is detected in Firebase in their appropriate sections.
        new DatabaseHelper(uniqueID).readPlants(new DatabaseHelper.DataStatus() {
            @Override
            public void DataLoaded(List<Plant> plantData) {
                findViewById(R.id.loading).setVisibility(View.VISIBLE);


                for(int i = 0; i < plantData.size(); i++){
                    dbInsertPlant.modifyPlant(plantData.get(i));
                }

                loadListView();
                findViewById(R.id.loading).setVisibility(View.INVISIBLE);
            }
        });

        plantNumber = findViewById(R.id.plantName);
        plantsListView = findViewById(R.id.plantsListView);

        linkPiButton = findViewById(R.id.linkPiButton);

        //Sends the user to the dialog fragment that will handle the request.
        linkPiButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                plants = dbInsertPlant.getAllPlants();

                ArrayList<String> getCurrentPlants = new ArrayList<>();

                for(int i = 0; i < plants.size(); i++) {
                    getCurrentPlants.add(plants.get(i).getName());
                }
                LinkPiDialogFragment dialog = new LinkPiDialogFragment();
                dialog.ownerID = uniqueID;
                dialog.currentPlants = getCurrentPlants;

                dialog.show(getSupportFragmentManager(), "LinkPiDialogFragment");
            }
        });


        addPlantFloatingButton = findViewById(R.id.addPlantFloatingButton);

        loadListView();

        //Sends the user to the dialog fragment that will handle the request.
        addPlantFloatingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                InsertPlantDialogFragment dialog = new InsertPlantDialogFragment();
                dialog.ownerID = uniqueID;

                dialog.show(getSupportFragmentManager(), "InsertPlantFragment");
            }
        });

        //This portion will send the user to the page that concerns the plant he/she pressed on in the Listview.
        plantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                goToChild(plants.get(position).getID(),uniqueID);
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Importance High makes the notification appear as a heads-up notif.
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH);

            //A muted notification.
            channel1.setSound(null,null);

            channel1.setDescription("This is channel 1");

            // Register the channel with the system; you can't change the importance or other notification behaviors after this.

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }

    //This method will send the user to the page that concerns the plant he/she pressed on in the Listview.
    protected void goToChild(long id, String user){
        Intent intent = new Intent(this, inspectPlantActivity.class);

        intent.putExtra("plantID", id);
        intent.putExtra("userID", user);
        startActivity(intent);
    }

    //This method updates the plants and their data in the Listview on the main page.
    protected void loadListView(){

        plants = dbInsertPlant.getAllPlants();

        plantCount = 0;
        double percentage = 0;

        ArrayList<String> plantTitles = new ArrayList<>();
        ArrayList<String> plantData = new ArrayList<>();
        ArrayList<Integer> plantImages = new ArrayList<>();
        ArrayList<Integer> plantPercents = new ArrayList<>();

        for(int i = 0; i < plants.size(); i++){

            plantCount++;
            String tempTitle = "";
            String tempData = "";
            int tempImages = R.drawable.notfound;

            int unicodeHappy = 0x1F60A;
            int unicodeAverage = 0x1F610;
            int unicodeSad = 0x1F641;

            //Smileys: 100%to75%:happy, 74%to50%:average, 49%&lower:sad
            String emojiHappy = getEmoji(unicodeHappy);
            String emojiAverage = getEmoji(unicodeAverage);
            String emojiSad = getEmoji(unicodeSad);


            tempTitle += plants.get(i).getName();

            tempData+= "Type: ";
            tempData+= plants.get(i).getType() + "\n";

            tempData+= "Moisture: ";

            //If no value was collected yet, 'N/A' will be displayed.
            if(plants.get(i).getMoisture() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                //Formats the data to have no decimals.
                tempData+= String.format("%.0f",(1023-plants.get(i).getMoisture())) + " units";

                //Depending on the plant type, a different criteria is used to detect how close are the data we collected to the desired values that the plant prefers.
                if(plants.get(i).getType().equals("Devil's Ivy")){
                    if(moistureSmileyDevilsIvy(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((moistureSmileyDevilsIvy(plants.get(i))<75) && (moistureSmileyDevilsIvy(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
                else if(plants.get(i).getType().equals("Sansevieria")){
                    if(moistureSmileySansevieria(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((moistureSmileySansevieria(plants.get(i))<75) && (moistureSmileySansevieria(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
                else if(plants.get(i).getType().equals("English Ivy")){
                    if(moistureSmileyEnglishIvy(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((moistureSmileyEnglishIvy(plants.get(i))<75) && (moistureSmileyEnglishIvy(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }

                else if(plants.get(i).getType().equals("Spider")){
                    if(moistureSmileySpider(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((moistureSmileySpider(plants.get(i))<75) && (moistureSmileySpider(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
            }

            tempData+= "UV Index: ";

            //If no value was collected yet, 'N/A' will be displayed.
            if(plants.get(i).getLightIntensity() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                //Formats the data to have no decimals.
                tempData+= String.format("%.0f",plants.get(i).getLightIntensity());

                //Depending on the plant type, a different criteria is used to detect how close are the data we collected to the desired values that the plant prefers.
                if(plants.get(i).getType().equals("Devil's Ivy")){
                    if(lightintensitySmileyDevilsIvy(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((lightintensitySmileyDevilsIvy(plants.get(i))<75) && (lightintensitySmileyDevilsIvy(plants.get(i))>=40)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
                else if(plants.get(i).getType().equals("Sansevieria")){
                    if(lightintensitySmileySansevieria(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((lightintensitySmileySansevieria(plants.get(i))<75) && (lightintensitySmileySansevieria(plants.get(i))>=40)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
                else if(plants.get(i).getType().equals("English Ivy")){
                    if(lightintensitySmileyEnglishIvy(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((lightintensitySmileyEnglishIvy(plants.get(i))<75) && (lightintensitySmileyEnglishIvy(plants.get(i))>=40)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }

                else if(plants.get(i).getType().equals("Spider")){
                    if(lightintensitySmileySpider(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((lightintensitySmileySpider(plants.get(i))<75) && (lightintensitySmileySpider(plants.get(i))>=40)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
            }

            tempData+= "Temperature: ";

            //If no value was collected yet, 'N/A' will be displayed.
            if(plants.get(i).getTemperature() <= -10000){
                tempData+= "N/A" + "\n";
            }
            else{
                //Formats the data to have one decimal value.
                tempData+= String.format("%.1f",plants.get(i).getTemperature()) + " °C";

                //Depending on the plant type, a different criteria is used to detect how close are the data we collected to the desired values that the plant prefers.
                if(plants.get(i).getType().equals("Devil's Ivy")){
                    if(tempSmileyDevilsIvy(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((tempSmileyDevilsIvy(plants.get(i))<75) && (tempSmileyDevilsIvy(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
                else if(plants.get(i).getType().equals("Sansevieria")){
                    if(tempSmileySansevieria(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((tempSmileySansevieria(plants.get(i))<75) && (tempSmileySansevieria(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
                else if(plants.get(i).getType().equals("English Ivy")){
                    if(tempSmileyEnglishIvy(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((tempSmileyEnglishIvy(plants.get(i))<75) && (tempSmileySansevieria(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }

                else if(plants.get(i).getType().equals("Spider")){
                    if(tempSmileySpider(plants.get(i))>=75){
                        tempData+= " " + emojiHappy + "\n";
                    }
                    else if((tempSmileySpider(plants.get(i))<75) && (tempSmileySpider(plants.get(i))>=50)){
                        tempData+= " " + emojiAverage + "\n";
                    }
                    else{
                        tempData+= " " + emojiSad + "\n";
                    }
                }
            }

            tempData+= "Health Percentage: ";

            //Depending on the plant type, a different criteria is used to calculate the plant's health.
            switch (plants.get(i).getType()) {
                case "Devil's Ivy": {
                    percentage = healthBarAlgoDevilsIvy(plants.get(i));

                    //If the plant's health falls below 50%, notify the user as an intervention is necessary.
                    if (percentage < 50 && percentage >=0) {
                        createNotificationChannel();
                        notificationBuilder.setSmallIcon(R.drawable.alert);
                        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.devilivy));
                        notificationBuilder.setContentTitle(plants.get(i).getName() + " needs your attention!");
                        notificationBuilder.setOnlyAlertOnce(true);
                        notificationBuilder.setOngoing(false);
                        notificationBuilder.setContentText("Your plant's health is lower than 50%!");
                        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                        //notificationId is a unique int for each notification that you must define
                        //4 and 5 is high and max importance respectively. Try 4 first

                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                        notificationBuilder.setContentIntent(contentIntent);
                        notificationManager.notify(4, notificationBuilder.build());
                    }

                    //If no value is collected yet, set the percentage as 0.
                    if(percentage < 0){
                        percentage = 0;
                    }

                    //Formats the health data to have one decimal value.
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.devilivy;

                    break;
                }
                case "Sansevieria": {
                    percentage = healthBarAlgoSansevieria(plants.get(i));

                    //If the plant's health falls below 50%, notify the user as an intervention is necessary.
                    if (percentage < 50 && percentage >=0) {
                        createNotificationChannel();
                        notificationBuilder.setSmallIcon(R.drawable.alert);
                        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sanseivieria));
                        notificationBuilder.setContentTitle(plants.get(i).getName() + " needs your attention!");
                        notificationBuilder.setContentText("Your plant's health is lower than 50%!");
                        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        notificationBuilder.setOnlyAlertOnce(true);
                        notificationBuilder.setOngoing(false);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                        //notificationId is a unique int for each notification that you must define
                        //4 and 5 is high and max importance respectively. Try 4 first

                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

                        notificationBuilder.setContentIntent(contentIntent);
                        notificationManager.notify(4, notificationBuilder.build());
                    }

                    //If no value is collected yet, set the percentage as 0.
                    if(percentage < 0){
                        percentage = 0;
                    }

                    //Formats the health data to have one decimal value.
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.sanseivieria;

                    break;
                }
                case "English Ivy": {
                    percentage = healthBarAlgoEnglishIvy(plants.get(i));

                    //If the plant's health falls below 50%, notify the user as an intervention is necessary.
                    if (percentage < 50 && percentage >=0) {
                        createNotificationChannel();
                        notificationBuilder.setSmallIcon(R.drawable.alert);
                        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.englishivy));
                        notificationBuilder.setContentTitle(plants.get(i).getName() + " needs your attention!");
                        notificationBuilder.setContentText("Your plant's health is lower than 50%!");
                        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        notificationBuilder.setOnlyAlertOnce(true);
                        notificationBuilder.setOngoing(false);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                        //notificationId is a unique int for each notification that you must define
                        //4 and 5 is high and max importance respectively. Try 4 first

                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


                        notificationBuilder.setContentIntent(contentIntent);
                        notificationManager.notify(4, notificationBuilder.build());
                    }

                    //If no value is collected yet, set the percentage as 0.
                    if(percentage < 0){
                        percentage = 0;
                    }

                    //Formats the health data to have one decimal value.
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.englishivy;
                    break;
                }
                case "Spider": {
                    percentage = healthBarAlgoSpider(plants.get(i));

                    //If the plant's health falls below 50%, notify the user as an intervention is necessary.
                    if (percentage < 50 && percentage >=0) {
                        createNotificationChannel();
                        notificationBuilder.setSmallIcon(R.drawable.alert);
                        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.spider));
                        notificationBuilder.setContentTitle(plants.get(i).getName() + " needs your attention!");
                        notificationBuilder.setContentText("Your plant's health is lower than 50%!");
                        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
                        notificationBuilder.setOnlyAlertOnce(true);
                        notificationBuilder.setOngoing(false);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                        //notificationId is a unique int for each notification that you must define
                        //4 and 5 is high and max importance respectively. Try 4 first

                        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


                        notificationBuilder.setContentIntent(contentIntent);
                        notificationManager.notify(4, notificationBuilder.build());
                    }

                    //If no value is collected yet, set the percentage as 0.
                    if(percentage < 0){
                        percentage = 0;
                    }

                    //Formats the health data to have one decimal value.
                    tempData += String.format("%.1f", percentage) + "%\n";

                    tempImages = R.drawable.spider;

                    break;
                }
                default:
                    break;
            }

            //Store the values collected.
            plantTitles.add(tempTitle);
            plantImages.add(tempImages);
            plantData.add(tempData);
            plantPercents.add((int)percentage);
        }

        //Lets the user keep track of how many plants there are on the app.
        if(plantCount == 1){
            String temp = "You currently have " + plantCount + " plant.";
            plantNumber.setText(temp);
        }
        else{
            String temp = "You currently have " + plantCount + " plants.";
            plantNumber.setText(temp);
        }

        //Send the values collected to our custom Listview.
        CustomListView customListView = new CustomListView(this,plantTitles,plantData,plantImages,plantPercents);

        plantsListView.setAdapter(customListView);
    }

    //An algorithm is used here that calculates the health percentage of the plant, which is a criteria we use to find the overall status of the plant.
    protected double healthBarAlgoSansevieria(Plant givenPlant){    //Specifically for Sansevieria

        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
        double FinalLightIntensityPercentage;

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }

        if ((1023-givenPlant.getMoisture()) >= 767){
            FinalMoisturePercentage = 100-(((1023-givenPlant.getMoisture())-767)*0.1953);           // 1023 pts = 50%
        }                                                                                           // 767 pts = 100%
        else{
            FinalMoisturePercentage = (1023-givenPlant.getMoisture()) * 0.1304;                    // 0 pts = 0 %
        }

		if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }

        return 0.33*FinalMoisturePercentage + 0.33*FinalTemperaturePercentage + 0.33*FinalLightIntensityPercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the moisture level of our plant from its desired value.
    protected double moistureSmileySansevieria(Plant givenPlant){   //Specifically for Sansevieria

        double FinalMoisturePercentage;

        if ((1023-givenPlant.getMoisture()) >= 767){
            FinalMoisturePercentage = 100-(((1023-givenPlant.getMoisture())-767)*0.1953);   // 1023 pts = 50%, 767 pts = 100% & 0 pts = 0 %
        }
        else{
            FinalMoisturePercentage = (1023-givenPlant.getMoisture()) * 0.1304;
        }

        return FinalMoisturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the temperature of our plant from its desired value.
    protected double tempSmileySansevieria(Plant givenPlant){   //Specifically for Sansevieria

        double FinalTemperaturePercentage;
        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }

        return FinalTemperaturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the UV index of our plant from its desired value.
	protected double lightintensitySmileySansevieria(Plant givenPlant){

        double FinalLightIntensityPercentage;

        if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }
        return FinalLightIntensityPercentage;
    }

    //An algorithm is used here that calculates the health percentage of the plant, which is a criteria we use to find the overall status of the plant.
    protected double healthBarAlgoEnglishIvy(Plant givenPlant){       //specifically for English Ivy/Ivy String

        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
		double FinalLightIntensityPercentage;

        if (givenPlant.getTemperature() >= 20){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-20)*3.75);       // 40deg C = 25%
        }                                                                                   // 20 deg C = 100%
        else{
            FinalTemperaturePercentage = 50+(givenPlant.getTemperature() * 2.5);            // -20deg C = 0%
        }

        if ((1023-givenPlant.getMoisture()) >= 256){
            FinalMoisturePercentage = Math.abs(100-(((1023-givenPlant.getMoisture())-256)*0.1304));     // 1023 pts (Dry) = 0%
        }                                                                                   // 256 pts = 100%
        else{
            FinalMoisturePercentage = 50 + ((1023-givenPlant.getMoisture()) * 0.1953);             // 0 pts (wet)  = 50%
        }

        if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }

        return 0.33*FinalMoisturePercentage + 0.33*FinalTemperaturePercentage + 0.33*FinalLightIntensityPercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the moisture level of our plant from its desired value.
    protected double moistureSmileyEnglishIvy(Plant givenPlant){       //specifically for English Ivy/Ivy String

        double FinalMoisturePercentage;

        if ((1023-givenPlant.getMoisture()) >= 256){
            FinalMoisturePercentage = Math.abs(100-(((1023-givenPlant.getMoisture())-256)*0.1304));     // 1023 pts (Dry) = 0%
        }                                                                                   // 256 pts = 100%
        else{
            FinalMoisturePercentage = 50 + ((1023-givenPlant.getMoisture()) * 0.1953);             // 0 pts (wet)  = 50%
        }

        return FinalMoisturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the temperature of our plant from its desired value.
    protected double tempSmileyEnglishIvy(Plant givenPlant){       //specifically for English Ivy/Ivy String

        double FinalTemperaturePercentage;

        if (givenPlant.getTemperature() >= 20){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-20)*3.75);       // 40deg C = 25%
        }                                                                                   // 20 deg C = 100%
        else{
            FinalTemperaturePercentage = 50+(givenPlant.getTemperature() * 2.5);            // -20deg C = 0%
        }

        return FinalTemperaturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the UV index of our plant from its desired value.
	protected double lightintensitySmileyEnglishIvy(Plant givenPlant){  //specifically for English Ivy/Ivy String

        double FinalLightIntensityPercentage;

        if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }
        return FinalLightIntensityPercentage;
    }

    //An algorithm is used here that calculates the health percentage of the plant, which is a criteria we use to find the overall status of the plant.
    protected double healthBarAlgoDevilsIvy(Plant givenPlant){       //specifically for Devil's Ivy

        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
        double FinalLightIntensityPercentage;

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }


        if ((1023-givenPlant.getMoisture()) >= 512){
            FinalMoisturePercentage = 100-(((1023-givenPlant.getMoisture())-512)*0.1953);      // 1023 pts (Dry) = 0%
        }                                                                               // 512 pts = 100%
        else{
            FinalMoisturePercentage = (1023-givenPlant.getMoisture()) * 0.1953;                // 0 pts (wet) = 0%
        }

		if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }

        return 0.33*FinalMoisturePercentage + 0.33*FinalTemperaturePercentage + 0.33*FinalLightIntensityPercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the moisture level of our plant from its desired value.
    protected double moistureSmileyDevilsIvy(Plant givenPlant){       //specifically for Devil's Ivy

        double FinalMoisturePercentage;

        if ((1023-givenPlant.getMoisture()) >= 512){
            FinalMoisturePercentage = 100-(((1023-givenPlant.getMoisture())-512)*0.1953);      // 1023 pts (Dry) = 0%
        }                                                                               // 512 pts = 100%
        else{
            FinalMoisturePercentage = (1023-givenPlant.getMoisture()) * 0.1953;                // 0 pts (wet) = 0%
        }

        return FinalMoisturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the temperature of our plant from its desired value.
    protected double tempSmileyDevilsIvy(Plant givenPlant){       //specifically for Devil's Ivy

        double FinalTemperaturePercentage;

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                  // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }

        return FinalTemperaturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the UV index of our plant from its desired value.
	protected double lightintensitySmileyDevilsIvy(Plant givenPlant){   //specifically for Devil's Ivy

        double FinalLightIntensityPercentage;
        if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }
        return FinalLightIntensityPercentage;
    }

    //An algorithm is used here that calculates the health percentage of the plant, which is a criteria we use to find the overall status of the plant.
    protected double healthBarAlgoSpider(Plant givenPlant){       //specifically for Spider
        double FinalTemperaturePercentage;
        double FinalMoisturePercentage;
        double FinalLightIntensityPercentage;

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }

        if ((1023-givenPlant.getMoisture()) >= 256){
            FinalMoisturePercentage = Math.abs(100-(((1023-givenPlant.getMoisture())-256)*0.1304));     // 1023 pts (Dry) = 0%
        }                                                                                   // 256 pts = 100%
        else{
            FinalMoisturePercentage = 50 + ((1023-givenPlant.getMoisture()) * 0.1953);             // 0 pts (wet)  = 50%
        }

		if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }

        return 0.33*FinalMoisturePercentage + 0.33*FinalTemperaturePercentage + 0.33*FinalLightIntensityPercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the moisture level of our plant from its desired value.
    protected double moistureSmileySpider(Plant givenPlant){       //specifically for Spider
        double FinalMoisturePercentage;

        if ((1023-givenPlant.getMoisture()) >= 256){
            FinalMoisturePercentage = Math.abs(100-(((1023-givenPlant.getMoisture())-256)*0.1304));     // 1023 pts (Dry) = 0%
        }                                                                                   // 256 pts = 100%
        else{
            FinalMoisturePercentage = 50 + ((1023-givenPlant.getMoisture()) * 0.1953);             // 0 pts (wet)  = 50%
        }

        return FinalMoisturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the temperature of our plant from its desired value.
    protected double tempSmileySpider(Plant givenPlant){       //specifically for Spider

        double FinalTemperaturePercentage;

        if (givenPlant.getTemperature() >= 23){
            FinalTemperaturePercentage = 100-((givenPlant.getTemperature()-23)*4.3478);     // 46 deg C = 0%
        }                                                                                   // 23 deg C = 100%
        else{
            FinalTemperaturePercentage = givenPlant.getTemperature() * 4.3478;              // 0 deg C = 0%
        }

        return FinalTemperaturePercentage;
    }

    //An algorithm is used here that gives us an insight on how far is the UV index of our plant from its desired value.
	protected double lightintensitySmileySpider(Plant givenPlant){  //specifically for Spider

        double FinalLightIntensityPercentage;

        if (givenPlant.getLightIntensity() == 0){                                           // case uv = 0
            FinalLightIntensityPercentage = 0;}
        else if (givenPlant.getLightIntensity()==1) {                                       // case uv = 1
            FinalLightIntensityPercentage = 100;}
        else if (givenPlant.getLightIntensity()==2) {                                       // case uv = 2
            FinalLightIntensityPercentage = 80;}
        else if (givenPlant.getLightIntensity()==3) {                                       // case uv = 3
            FinalLightIntensityPercentage = 40;}
        else if (givenPlant.getLightIntensity()==4) {                                       // case uv = 4
            FinalLightIntensityPercentage = 20;}
        else{                                                                               // all other cases
            FinalLightIntensityPercentage = 0;
        }
        return FinalLightIntensityPercentage;
    }

    //This method is used the extract the Emoji image from the phone.
    protected String getEmoji(int unicode){
        return new String(Character.toChars(unicode));
    }


    //Every time the app runs, the List's data needs to be re-collected in order to have the latest data.
    @Override
    protected void onStart() {
        super.onStart();

        loadListView();
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



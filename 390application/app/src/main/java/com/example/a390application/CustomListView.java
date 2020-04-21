package com.example.a390application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

//This is used as an Adapter for the Listview on the main page of the app in order to format the List into the custom style we created.
public class CustomListView extends ArrayAdapter<String> {
    private ArrayList<String> plantNames;
    private ArrayList<String> plantData;
    private ArrayList<Integer> correspondingImage;
    private ArrayList<Integer> plantPercents;
    private Activity context;

    CustomListView(Activity context, ArrayList<String> plantNames, ArrayList<String> plantData, ArrayList<Integer> correspondingImage, ArrayList<Integer> plantPercents){
        super(context, R.layout.custom_list_view, plantNames);

        this.context = context;
        this.plantNames = plantNames;
        this.plantData = plantData;
        this.correspondingImage = correspondingImage;
        this.plantPercents = plantPercents;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.custom_list_view,null,true);
            viewHolder = new ViewHolder(r);

            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.title.setText(plantNames.get(position));
        viewHolder.data.setText(plantData.get(position));
        viewHolder.icon.setImageResource(correspondingImage.get(position));
        viewHolder.healthPercentage.setProgress(plantPercents.get(position));

        return r;
    }

    static class ViewHolder{
        TextView title;
        TextView data;
        ImageView icon;
        ProgressBar healthPercentage;

        ViewHolder(View v){
            title = v.findViewById(R.id.plantNameTitle);
            data = v.findViewById(R.id.plantInformation);
            icon = v.findViewById(R.id.imageOfPlantInList);
            healthPercentage = v.findViewById(R.id.health_percentage);
        }

    }
}

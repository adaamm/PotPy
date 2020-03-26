package com.example.a390application;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<String> {
    private ArrayList<String> plantNames;
    private ArrayList<String> plantData;
    private ArrayList<Integer> correspondingImage;
    private Activity context;

    public CustomListView(Activity context, ArrayList<String> plantNames, ArrayList<String> plantData, ArrayList<Integer> correspondingImage){
        super(context, R.layout.custom_list_view, plantNames);

        this.context = context;
        this.plantNames = plantNames;
        this.plantData = plantData;
        this.correspondingImage = correspondingImage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;
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

        return r;
    }

    class ViewHolder{
        TextView title;
        TextView data;
        ImageView icon;

        ViewHolder(View v){
            title = (TextView) v.findViewById(R.id.plantNameTitle);
            data = (TextView) v.findViewById(R.id.plantInformation);
            icon = (ImageView) v.findViewById(R.id.imageOfPlantInList);
        }

    }
}

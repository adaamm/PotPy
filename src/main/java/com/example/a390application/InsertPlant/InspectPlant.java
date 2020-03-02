package com.example.a390application.InsertPlant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InspectPlant extends SQLiteOpenHelper {

    private Context context;

    public InspectPlant(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*public long insertPlant(Plant plant){
        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.COLUMN_PLANT_NAME, plant.getName());
        contentValues.put(Config.COLUMN_PLANT_MOISTURE, plant.getMoisture());
        contentValues.put(Config.COLUMN_PLANT_PH, plant.getPh());

        try{
            id = db.insertOrThrow(Config.PLANT_TABLE_NAME, null, contentValues);
        }
        catch(SQLiteException e){
            Toast.makeText(context, "Operation Failed!: " + e, Toast.LENGTH_LONG).show();
        }
        finally{
            db.close();
        }
        return id;
    }*/

}

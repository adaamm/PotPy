
package com.example.a390application.InsertPlant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InsertPlant extends SQLiteOpenHelper {

    private Context context;

    public InsertPlant(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PLANT = "CREATE TABLE " + Config.PLANT_TABLE_NAME + " (" + Config.COLUMN_PLANT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_PLANT_NAME + " TEXT NOT NULL, "
                + Config.COLUMN_PLANT_TYPE + " TEXT NOT NULL, "
                + Config.COLUMN_PLANT_MOISTURE + " DOUBLE, "
                + Config.COLUMN_PLANT_LIGHT_INTENSITY + " DOUBLE, "
                + Config.COLUMN_PLANT_TEST + " TEXT)";

        db.execSQL(CREATE_TABLE_PLANT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertPlant(Plant plant) {
        long id = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.COLUMN_PLANT_NAME, plant.getName());
        contentValues.put(Config.COLUMN_PLANT_TYPE, plant.getType());
        contentValues.put(Config.COLUMN_PLANT_MOISTURE, plant.getMoisture());
        contentValues.put(Config.COLUMN_PLANT_LIGHT_INTENSITY, plant.getLightIntensity());
        contentValues.put(Config.COLUMN_PLANT_TEST, plant.getTest());


        try {
            id = db.insertOrThrow(Config.PLANT_TABLE_NAME, null, contentValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, "Operation Failed!: " + e, Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
        return id;
    }

    public List<Plant> getAllPlants() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {
            cursor = db.query(Config.PLANT_TABLE_NAME, null, null, null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    List<Plant> plants = new ArrayList<>();

                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(Config.COLUMN_PLANT_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANT_NAME));
                        String type = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANT_TYPE));
                        double moisture = cursor.getDouble(cursor.getColumnIndex(Config.COLUMN_PLANT_MOISTURE));
                        double lightIntensity = cursor.getDouble((cursor.getColumnIndex(Config.COLUMN_PLANT_LIGHT_INTENSITY)));
                        String test = cursor.getString(cursor.getColumnIndex(Config.COLUMN_PLANT_TEST));

                        plants.add(new Plant(id, name, type, moisture, lightIntensity,test));

                    } while (cursor.moveToNext());

                    return plants;
                }
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "Operation Failed!: " + e, Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return Collections.emptyList();
    }

    public void modifyPlant(Plant newPlantData) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Config.COLUMN_PLANT_NAME, newPlantData.getName());
        contentValues.put(Config.COLUMN_PLANT_TYPE, newPlantData.getType());
        contentValues.put(Config.COLUMN_PLANT_MOISTURE, newPlantData.getMoisture());
        contentValues.put(Config.COLUMN_PLANT_LIGHT_INTENSITY, newPlantData.getLightIntensity());
        contentValues.put(Config.COLUMN_PLANT_TEST, newPlantData.getTest());


        try {
            db.update(Config.PLANT_TABLE_NAME, contentValues, Config.COLUMN_PLANT_NAME + "=?", new String[]{newPlantData.getName()});
        }
        catch (SQLiteException e) {
            Toast.makeText(context, "Operation Failed!: " + e, Toast.LENGTH_LONG).show();
        }
        finally {
            db.close();
        }
    }
}

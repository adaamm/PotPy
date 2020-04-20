
package com.example.a390application.InsertPlant;

//This class is used to set up the SQL tables which were used to store data locally.
class Config {

    static final String DATABASE_NAME = "Plants-db";
    static final int DATABASE_VERSION = 1;

    static final String PLANT_TABLE_NAME = "plants";

    static final String COLUMN_PLANT_ID = "_id";
    static final String COLUMN_PLANT_NAME = "name";
    static final String COLUMN_PLANT_TYPE = "type";
    static final String COLUMN_PLANT_MOISTURE = "moisture";
    static final String COLUMN_PLANT_LIGHT_INTENSITY = "light";

    static final String COLUMN_PLANT_HUMIDITY = "humidity";
    static final String COLUMN_PLANT_TEMPERATURE = "temperature";
    static final String COLUMN_PLANT_OWNER = "owner";

    static final String UNIQUE_ID_TABLE = "userID";
    static final String COLUMN_UID_ID = "_id1";
    static final String COLUMN_UNIQUE_ID = "uid";
}

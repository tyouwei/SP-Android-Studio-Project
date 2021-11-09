package com.sp.p1722240;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locatonlist.db";
    private static final int SCHEMA_VERSION = 1;

    public TripHelper(Context context) {
        super (context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Wil be called once when the database is not created
        db.execSQL("CREATE TABLE trip_table( _id INTEGER PRIMARY KEY AUTOINCREMENT, locationName TEXT, lat REAL, lon REAL);");

}
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Will not be called until SCHEMA_VERSION increases
        //Here we can upgrade the database e.g. add more tables
    }
    //Read all records from restaurants_table
    public Cursor getAll(String orderBy) {
        return (getReadableDatabase().rawQuery("SELECT _id, locationName, lat, lon FROM trip_table ORDER BY " + orderBy, null));
    }

    public Cursor getById(String id) {
        String[] args = {id};
        return(getReadableDatabase().rawQuery("SELECT _id, locationName, lat, lon FROM trip_table WHERE _ID = ?", args));
    }

    //Write a record into restaurants_table
    public void insert (String locationName, double lat, double lon) {
        ContentValues cv = new ContentValues();

        cv.put ("locationName", locationName);
        cv.put ("lat", lat);
        cv.put ("lon", lon);

        getWritableDatabase().insert("trip_table", "locationName", cv);
    }

    public void update (String id, String locationName, double lat, double lon) {
        ContentValues cv = new ContentValues();
        String[] args = {id};
        cv.put("locationName",locationName);
        cv.put("lat", lat);
        cv.put("lon",lon);
        getWritableDatabase().update("trip_table", cv, " _ID = ?", args);
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {id};
        db.delete("trip_table"," _ID = ?", args);
    }

    public String getID(Cursor c){
        return(c.getString(0));
    }
    public String getLocationName (Cursor c) {
        return (c.getString(1));
    }
    public double getLatitude(Cursor c) {
        return(c.getDouble(2));
    }
    public double getLongitude(Cursor c) {
        return(c.getDouble(3));
    }
}


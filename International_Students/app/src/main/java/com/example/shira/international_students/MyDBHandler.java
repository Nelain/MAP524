package com.example.shira.international_students;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelan Bo Li on 2015-08-11.
 * This class handles all database related activities
 * No database related objects (e.g. cursor) are passed outside of this class
 */
public class MyDBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DB";
    private static final String DATABASE_NAME = "map524.db";
    private static final int DATABASE_VERSION = 1;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // Get a list of orgin countries or Canadian (destination) provinces,
    // with only their names and unique identifier code (ISO)
    public ArrayList<Region> getRegions(String table) {

        String name, iso;
        ArrayList<Region> provinces = new ArrayList<Region>();

        // Open database connection
        SQLiteDatabase db = this.getReadableDatabase();

        //Query statement
        Cursor tableCursor = db.rawQuery("SELECT * FROM " + table, null);

        // Retrieve all names from the query result
        if (tableCursor != null) {
            tableCursor.moveToFirst();
            do {
                name = tableCursor.getString(tableCursor.getColumnIndexOrThrow("_name"));
                iso = tableCursor.getString(tableCursor.getColumnIndexOrThrow("_iso"));
                provinces.add(new Region(name, iso));
            } while (tableCursor.moveToNext());
            tableCursor.close();
        }
        db.close();
        return provinces;
    }



    // http://guides.codepath.com/android/Local-Databases-with-SQLiteOpenHelper
    // Returns a single country/province object
    public Region getRegion(String table, String name) {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Region result = null;
        Cursor cursor;
        Log.d("DB", table + " " + name);
        cursor = db.rawQuery( "SELECT * FROM " + table +
                " WHERE _name=?", new String[] { name } );

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            // Load result into model object
            result = new Region(cursor.getString(cursor.getColumnIndexOrThrow("_name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("_iso")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2013_q1")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2013_q2")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2013_q3")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2013_q4")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2013_total")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2014_q1")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2014_q2")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2014_q3")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2014_q4")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2014_total")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2013_rank")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("_2014_rank")));
        }
        if (cursor != null)
            cursor.close();
        db.close();

        // return item
        return result;
    }
}

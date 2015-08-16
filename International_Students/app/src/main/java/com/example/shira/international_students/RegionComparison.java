package com.example.shira.international_students;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class RegionComparison extends AppCompatActivity {

    static MyDBHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_comparison);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Get the region name from the intent that started this activity
        final String type = this.getIntent().getStringExtra("Type");

        // Use MyDBHandler to interact with database
        myDB = new MyDBHandler(this);

        // Construct the data source for the spinners
        ArrayList<Region> regions = myDB.getRegions(type);

        // Create the adapter to convert the array to views
        //https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        //http://mrbool.com/how-to-customize-spinner-in-android/28286
        RegionsAdapter adapter = new RegionsAdapter(this, regions);

        // Attach the adapter to spinners
        Spinner sp1 = (Spinner) findViewById(R.id.spinner1);
        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        sp1.setAdapter(adapter);
        sp2.setAdapter(adapter);

        if (type.equals(getString(R.string.table_countries))) {
            setTitle("Comparing countries of origin");
            //Set initial selection to display
            setView(type, true, "People's Republic of China");
            setView(type, false, "India");
            sp1.setSelection(117); //China
            sp2.setSelection(71); //India
        } else {
            setTitle("Comparing destination provinces");
            setView(type, true, "Ontario");
            setView(type, false, "Quebec");
            sp1.setSelection(7); //Ontario
            sp2.setSelection(9); //Quebec
        }

        //http://www.mkyong.com/android/android-spinner-drop-down-list-example/
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String name = ((TextView) view.findViewById(R.id.list_name)).getText().toString();
                setView(type, true, name);
            }
        });

        //http://www.mkyong.com/android/android-spinner-drop-down-list-example/
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String name = ((TextView) view.findViewById(R.id.list_name)).getText().toString();
                setView(type, false, name);
            }
        });
    }

    /**
     * get data from DB then set them in the layout views
     * @param type countries or provinces
     * @param flag true to set in top compared region, false to set in the bottom compared region
     * @param name to targeted region
     */
    private void setView(String type, boolean flag, String name) {

        Region region = myDB.getRegion(type, name);

        if (flag) {
            // http://stackoverflow.com/questions/5760751/android-variable-passed-for-r-drawable-variablevalue
            // Set the flag
            ((ImageView) findViewById(R.id.comFlag1)).setImageResource(
                    getApplicationContext().getResources().getIdentifier(
                            region.get_iso(), "drawable", getApplicationContext().getPackageName()));
            // Set the rank and total
            ((TextView) findViewById(R.id.com2013Rank1)).setText("2013 Rank: " + Integer.toString(region.get_2013_rank()));
            ((TextView) findViewById(R.id.com2013Total1)).setText("Total: " + Integer.toString(region.get_2013_total()));
            ((TextView) findViewById(R.id.com2014Rank1)).setText("2014 Rank: " + Integer.toString(region.get_2014_rank()));
            ((TextView) findViewById(R.id.com2014Total1)).setText("Total: " + Integer.toString(region.get_2014_total()));
        } else {
            // http://stackoverflow.com/questions/5760751/android-variable-passed-for-r-drawable-variablevalue
            // Set the flag
            ((ImageView) findViewById(R.id.comFlag2)).setImageResource(
                    getApplicationContext().getResources().getIdentifier(
                            region.get_iso(), "drawable", getApplicationContext().getPackageName()));
            // Set the rank and total
            ((TextView) findViewById(R.id.com2013Rank2)).setText("2013 Rank: " + Integer.toString(region.get_2013_rank()));
            ((TextView) findViewById(R.id.com2013Total2)).setText("Total: " + Integer.toString(region.get_2013_total()));
            ((TextView) findViewById(R.id.com2014Rank2)).setText("2014 Rank: " + Integer.toString(region.get_2014_rank()));
            ((TextView) findViewById(R.id.com2014Total2)).setText("Total: " + Integer.toString(region.get_2014_total()));
        }
    }

}

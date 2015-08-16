package com.example.shira.international_students;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    static final String TAG = "App";
    static final String DB_FILE_NAME = "map524.db";
    static MyDBHandler myDB;

    /**
     * Load db file into app
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("International Students");

        // Import db file into app, this is the code given in lab 6
        try
        {
            String destPath = "/data/data/" + this.getPackageName() + "/databases/";

            // Create des. dir if needed
            File destPathFile =  new File(destPath);
            if (!destPathFile.exists())
                destPathFile.mkdirs();

            File destFile = new File(destPath + DB_FILE_NAME);
            if (!destFile.exists())
            {
                Log.d(TAG, "First run, copying default database");
                copyFile(this.getAssets().open(DB_FILE_NAME),
                        new FileOutputStream(destPath + DB_FILE_NAME));
            }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

    }

    /**
     * 1. Populate the AutoCompleteTextView, load detail activity once search term selected
     * 2. Listen to user click, send intent to proper activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Use MyDBHandler to interact with database
        myDB = new MyDBHandler(this);

        // Construct the data source
        ArrayList<Region> regions1 = myDB.getRegions(getString(R.string.table_countries));
        ArrayList<Region> regions2 = myDB.getRegions(getString(R.string.table_provinces));
        // Combine the 2 region list
        // http://www.roseindia.net/tutorial/java/collections/arraylist/merge.html
        regions1.addAll(regions2);

        // Extract the list of names from the combined region list
        ArrayList<String> regionNames = new ArrayList<>();
        for (int i=0; i<regions1.size(); i++) {
            regionNames.add(regions1.get(i).get_name());
        }

        //https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        //https://androiddesk.wordpress.com/2012/03/13/autocompletetextview-in-android/
        ArrayAdapter<String> adapter= new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, regionNames);

        AutoCompleteTextView autoSearch = (AutoCompleteTextView)findViewById(R.id.searchRegion);
        autoSearch.setText("");
        autoSearch.setThreshold(2);
        autoSearch.setAdapter(adapter);

        autoSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                String name = ((TextView) view).getText().toString();
                Region region = myDB.getRegion(getString(R.string.table_provinces), name);
                Intent i10 = new Intent(MainActivity.this, RegionDetailActivity.class);
                //Check if the region name is a province, and send the proper type (table name)
                if (region == null)
                    i10.putExtra("Type", getString(R.string.table_countries));
                else
                    i10.putExtra("Type", getString(R.string.table_provinces));
                i10.putExtra("Region", name);
                startActivity(i10);
            }
        });

        Button b = (Button)findViewById(R.id.button_countryList);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(MainActivity.this, RegionActivity.class);
                i3.putExtra("Type", getString(R.string.table_countries));
                startActivity(i3);
            }
        });

        Button b1 = (Button)findViewById(R.id.button_regionList);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(MainActivity.this, RegionActivity.class);
                i4.putExtra("Type", getString(R.string.table_provinces));
                startActivity(i4);
            }
        });

        Button b2 = (Button)findViewById(R.id.button_compareCountries);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(MainActivity.this, RegionComparison.class);
                i5.putExtra("Type", getString(R.string.table_countries));
                startActivity(i5);
            }
        });

        Button b3 = (Button)findViewById(R.id.button_compareRegions);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i6 = new Intent(MainActivity.this, RegionComparison.class);
                i6.putExtra("Type", getString(R.string.table_provinces));
                startActivity(i6);
            }
        });

    }

    // Method used to copy file, code copied lab 06
    public void copyFile(InputStream instream, OutputStream outstream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = instream.read(buffer)) > 0)
            outstream.write(buffer, 0, length);
        instream.close();
        instream.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}

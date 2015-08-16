package com.example.shira.international_students;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * This really should be called RegionList
 * It is the activity that populate the listview
 */
public class RegionActivity extends ActionBarActivity {

    static MyDBHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        //Get the type (country or province) from the intent that started this activity
        final String type = this.getIntent().getStringExtra("Type");
        setTitle("List of " + type);

        // Use MyDBHandler to interact with database
        myDB = new MyDBHandler(this);

        // Construct the data source
        ArrayList<Region> regions = myDB.getRegions(type);

        // Create the adapter to convert the array to views
        // custom adapter used so list can display small flag icons
        //https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
        RegionsAdapter adapter = new RegionsAdapter(this, regions);

        // Attach the adapter to a ListView
        ListView lv = (ListView) findViewById(R.id.listView_region);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                //use intent to start detailed region activity and pass the country name
                Intent toRegionDetail = new Intent(RegionActivity.this, RegionDetailActivity.class);
                String name = ((TextView) view.findViewById(R.id.list_name)).getText().toString();
                toRegionDetail.putExtra("Type", type);
                toRegionDetail.putExtra("Region", name);
                startActivity(toRegionDetail);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_region, menu);
        return true;
    }
}

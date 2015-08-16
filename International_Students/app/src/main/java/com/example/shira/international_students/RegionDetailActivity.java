package com.example.shira.international_students;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

/**
 * Display detailed data for each region, including a graph
 */
public class RegionDetailActivity extends ActionBarActivity {

    static MyDBHandler myDB;

    /**
     * 1. Decide if the region is a country or province
     * 2. For country, load flag from remote url
     * 3. For province, load flag from internal storage
     * 4. Populate non-calculated fields (views) with data
     * 5. Populate calculated fields
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_detail);

        //Get the region name from the intent that started this activity
        String type = this.getIntent().getStringExtra("Type");
        String name = this.getIntent().getStringExtra("Region");
        ((TextView) findViewById(R.id.regionName)).setText(name);

        // Use MyDBHandler to interact with database
        myDB = new MyDBHandler(this);
        Region region = myDB.getRegion(type, name);

        if (type.equals(getString(R.string.table_countries))) {
            setTitle("Origin - " + name);
            //http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
            //Get country flags form online resource http://www.geonames.org/
            if (!region.get_iso().equals("none")) {
                //The Dominican Republic's ISO code is 'DO', which has special meaning for android
                //In order for the code to run, I had to change the ISO code to 'DY', hence this special case
                if (region.get_iso().equals("dy")) {
                    new DownloadImageTask((ImageView) findViewById(R.id.regionFlag))
                            .execute("http://www.geonames.org/flags/x/do.gif");
                } else {
                    new DownloadImageTask((ImageView) findViewById(R.id.regionFlag))
                            .execute("http://www.geonames.org/flags/x/" + region.get_iso() + ".gif");
                }
            }
        } else {
            setTitle("Destination - " + name);
            // http://stackoverflow.com/questions/5760751/android-variable-passed-for-r-drawable-variablevalue
            // Get the Canadian flags from internal storage
            ((ImageView) findViewById(R.id.regionFlag)).setImageResource(
                    getApplicationContext().getResources().getIdentifier(
                            region.get_iso(), "drawable", getApplicationContext().getPackageName()));
        }

        //Set the ranking
        ((TextView) findViewById(R.id.rank_2013)).setText("2013 Rank: " + region.get_2013_rank());
        ((TextView) findViewById(R.id.rank_2014)).setText("2014 Rank: " + region.get_2014_rank());

        //Set table data - non calculated
        ((TextView) findViewById(R.id.cCell2_2)).setText(Integer.toString(region.get_2013_q1()));
        ((TextView) findViewById(R.id.cCell2_3)).setText(Integer.toString(region.get_2013_q2()));
        ((TextView) findViewById(R.id.cCell2_4)).setText(Integer.toString(region.get_2013_q3()));
        ((TextView) findViewById(R.id.cCell2_5)).setText(Integer.toString(region.get_2013_q4()));
        ((TextView) findViewById(R.id.cCell2_6)).setText(Integer.toString(region.get_2013_total()));
        ((TextView) findViewById(R.id.cCell3_2)).setText(Integer.toString(region.get_2014_q1()));
        ((TextView) findViewById(R.id.cCell3_3)).setText(Integer.toString(region.get_2014_q2()));
        ((TextView) findViewById(R.id.cCell3_4)).setText(Integer.toString(region.get_2014_q3()));
        ((TextView) findViewById(R.id.cCell3_5)).setText(Integer.toString(region.get_2014_q4()));
        ((TextView) findViewById(R.id.cCell3_6)).setText(Integer.toString(region.get_2014_total()));

        //Set table data - calculated field
        //Set textcolor depend on the value of calculated field
        formatChange(R.id.cCell4_2,region.get_2013_q1(), region.get_2014_q1());
        formatChange(R.id.cCell4_3,region.get_2013_q2(), region.get_2014_q2());
        formatChange(R.id.cCell4_4,region.get_2013_q3(), region.get_2014_q3());
        formatChange(R.id.cCell4_5,region.get_2013_q4(), region.get_2014_q4());
        formatChange(R.id.cCell4_6,region.get_2013_total(), region.get_2014_total());

        //http://www.android-graphview.org/documentation/how-to-create-a-simple-graph
        //http://www.android-graphview.org/documentation/category/bar-graph
        GraphView graph = (GraphView) findViewById(R.id.regionGraph);
        BarGraphSeries<DataPoint> series1 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, region.get_2013_q1()),
                new DataPoint(2, region.get_2013_q2()),
                new DataPoint(3, region.get_2013_q3()),
                new DataPoint(4, region.get_2013_q4()),
                new DataPoint(5, region.get_2013_total()),
                new DataPoint(6, 0)
        });
        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, region.get_2014_q1()),
                new DataPoint(2, region.get_2014_q2()),
                new DataPoint(3, region.get_2014_q3()),
                new DataPoint(4, region.get_2014_q4()),
                new DataPoint(5, region.get_2014_total()),
                new DataPoint(6, 0)
        });
        graph.addSeries(series1);
        graph.addSeries(series2);

        //http://www.android-graphview.org/documentation/legend
        series1.setColor(Color.DKGRAY);
        series1.setTitle("2013");
        series2.setTitle("2014");
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        series1.setSpacing(50);
        series2.setSpacing(50);
        //http://www.android-graphview.org/documentation/category/labels-and-label-formatter
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{" ", "Q1", "Q2", "Q3", "Q4", "Total", " "});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    /**
     * Set table data - calculated field
     * Set textcolor depend on the value of calculated field
     * @param cid the id of the view to place the calculated data in
     * @param _2013 value for 2013 (Q1, Q2, Q3, Q4, Total)
     * @param _2014 corresponding value for 2014 (Q1, Q2, Q3, Q4, Total)
     */
    private void formatChange(int cid, int _2013, int _2014) {
        if (_2013 != 0) {
            float change = (float) (_2014 - _2013) / _2013 * 100;
            //http://stackoverflow.com/questions/2538787/how-to-display-an-output-of-float-data-with-2-decimal-places-in-java
            String formattedString = String.format("%.01f", change);
            ((TextView) findViewById(cid)).setText(formattedString + "%");
            //http://www.color-hex.com/color/006400
            String textColor = change < 0 ? "#ff0000" : "#006400";
            //http://stackoverflow.com/questions/4602902/how-to-set-the-text-color-of-textview-in-code
            ((TextView) findViewById(cid)).setTextColor(Color.parseColor(textColor));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_region_detail, menu);
        return true;
    }
}

package com.example.shira.international_students;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shira on 2015-08-13.
 * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 * This custom arrayAdapter is used so the listview and spinner can display flag icons
 */
public class RegionsAdapter extends ArrayAdapter<Region> {
    public RegionsAdapter(Context context, ArrayList<Region> regions) {
        super(context, 0, regions);
    }

    //http://mrbool.com/how-to-customize-spinner-in-android/28286
    //this method is needed for the custom arraydapter to work with spinners
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Region region = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        // Lookup view for data population
        ImageView icon = (ImageView) convertView.findViewById(R.id.list_icon);
        TextView name = (TextView) convertView.findViewById(R.id.list_name);

        // Populate the data into the template view using the data object
        name.setText(region.get_name());
        // http://stackoverflow.com/questions/5760751/android-variable-passed-for-r-drawable-variablevalue
        String iconName = region.get_iso() + "_s";
        icon.setImageResource(getContext().getResources().getIdentifier(iconName, "drawable", getContext().getPackageName()));
        // Return the completed view to render on screen
        return convertView;
    }

}

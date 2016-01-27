package com.patcomp.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by patrick on 1/25/16.
 */
public class ItemAdapter extends ArrayAdapter<Item> {
    public ItemAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvLevel = (TextView) convertView.findViewById(R.id.tvLevel);
        // Populate the data into the template view using the data object
        tvName.setText(item.Name);
        tvLevel.setText(item.level);

        if (item.level.equals("LOW"))
            tvLevel.setTextColor(Color.BLUE);
        else if (item.level.equals("MEDIUM"))
            tvLevel.setTextColor(Color.GREEN);
        else if (item.level.equals("HIGH"))
        tvLevel.setTextColor(Color.RED);

        // Return the completed view to render on screen
        return convertView;
    }
}
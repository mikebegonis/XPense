package com.bekoal.xpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by begon_000 on 4/30/2015.
 */
public class TripSpinnerAdapter extends ArrayAdapter<String> {

    private final List<Trip> mTrips = new ArrayList<Trip>();


    public void add(Trip trip) {
        mTrips.add(trip);
        super.add(String.format("%s %s", trip.getmTitle(), trip.getmStartDate()));
    }

    public TripSpinnerAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TripSpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public TripSpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    public TripSpinnerAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public TripSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    public TripSpinnerAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        Trip trip = (Trip)mTrips.get(position);
        view.setTag(trip);

        return view;
    }
}

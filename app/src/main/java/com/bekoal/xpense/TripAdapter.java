package com.bekoal.xpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 4/8/2015.
 */
public class TripAdapter extends BaseAdapter {

    private final List<Trip>mTrips = new ArrayList<Trip>();
    private LayoutInflater mInflater;
    private final Context mContext;

    public TripAdapter(Context context){
        mContext = context;
    }

    public void addTrip(Trip trip){
        mTrips.add(trip);
        notifyDataSetChanged();
    }

    public long getItemId(int pos) {
        return pos;
    }

    public Object getItem(int pos) {
        return mTrips.get(pos);
    }

    public int getCount(){
        return mTrips.size();
    }



    public View getView(int pos, View resultingView, ViewGroup parent){
        final Trip trip = (Trip) getItem(pos);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resultingView = mInflater.inflate(R.layout.activity_trip, null);

        final TextView titleView = (TextView) resultingView.findViewById(R.id.title);
        String titleText = trip.getmTitle().toString();
        titleView.setText(titleText);

        final TextView startDateView = (TextView) resultingView.findViewById(R.id.startDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String startDateText = simpleDateFormat.format(trip.getmStartDate());
        startDateView.setText(startDateText);

        final TextView endDateView = (TextView) resultingView.findViewById(R.id.endDate);
        String endDateText = simpleDateFormat.format(trip.getmEndDate());
        endDateView.setText(endDateText);

        final TextView totalView = (TextView) resultingView.findViewById(R.id.total);
        String totalText = trip.getmTotal();
        totalView.setText(totalText);

        final TextView noteView = (TextView) resultingView.findViewById(R.id.note);
        String noteText = trip.getmNote().replace("//","'");
        noteView.setText(noteText);

        resultingView.setTag(trip);

        return resultingView;
    }

}

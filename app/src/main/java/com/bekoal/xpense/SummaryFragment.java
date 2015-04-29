package com.bekoal.xpense;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.ListActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.app.ListFragment;

import com.bekoal.xpense.service.DatabaseHelper;

import java.math.BigDecimal;

public class SummaryFragment extends ListFragment {

    OnTravelSelectedListener mCallback;
    private BroadcastReceiver _reciever = null;
    TripAdapter mAdapter;
    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private int layout;
    private LinearLayout background = null;


    public interface OnTravelSelectedListener{
        public void onTripSelected(String query);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            mCallback = (OnTravelSelectedListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must implement OnTripSelectedListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();
        mAdapter = new TripAdapter(getActivity().getApplicationContext());
        setListAdapter(mAdapter);
        background = (LinearLayout) getActivity().findViewById(R.id.background);

        background.setPadding(0, 0, 0, 0);

        Cursor c = db.rawQuery("SELECT * FROM Travel", null);
        while(c.moveToNext()) {
            if (c.moveToNext()) {
                String[] args = new String[c.getColumnCount() + 1];
                for (int i = 0; i < c.getColumnCount(); i++) {
                    args[i] = c.getString(i);
                }

                String travelID = args[5];
                String queryString = new String();
                queryString = "SELECT SUM(Amount) FROM Expenses WHERE TravelID = " + travelID;
                Cursor c2 = db.rawQuery(queryString, null);

                if(c2.moveToFirst()) {
                    // Note: It would appear that the database has 2 of every entry in it
                    // for some reason, but only 1 of each appears on the Summary pane which
                    // is quite convenient. To make sure our running total isn't double what it
                    // should be as a result of this bug, I divide by 2.
                    Double sum = c2.getDouble(0)/2;
                    sum = (double) Math.round(sum * 100.0);
                    sum = sum/100;

                    args[6] = Double.toString(sum);
                 }
                else{
                    args[6] = "error";
                }
                     //   args[6] = Integer.toString(c2.getInt(0));
                     //   args[6] = c2.getString(0);

               // args[6] = travelID;
                Trip newTrip = new Trip(args);
                mAdapter.addTrip(newTrip);
            }
        }
        return;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        super.onListItemClick(listView, view, position, id);

        Trip currentTrip = (Trip) listView.getItemAtPosition(position);
        String travelID = currentTrip.getmTravelID();
        String queryString = "SELECT * FROM Expenses WHERE TravelID = " + travelID;

        mCallback.onTripSelected(queryString);


//        expenseAdapter = new ExpenseAdapter(getActivity().getApplicationContext());
//        setListAdapter(expenseAdapter);
//
//        Cursor c = db.rawQuery(queryString, null);
//        while(c.moveToNext()) {
//            if (c.moveToNext()) {
//                String[] args = new String[c.getColumnCount()];
//                for (int i = 0; i < c.getColumnCount(); i++) {
//                    args[i] = c.getString(i);
//                }
//                Expense newExpense = new Expense(args);
//                expenseAdapter.addExpense(newExpense);
//            }
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use different layout definition, depending on whether device is pre-
        // or post-honeycomb

        layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
                : android.R.layout.simple_list_item_1;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        // Allows users to edit or delete trips
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Modify trip?");
                dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Edit the trip
                        dialog.cancel();
                    }
                });

                dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the trip from the list and the db?
                        // Maybe add a confirm alert?
                        dialog.cancel();
                    }
                });

                dialog.create().show();

                return true;
            }
        });
    }
}

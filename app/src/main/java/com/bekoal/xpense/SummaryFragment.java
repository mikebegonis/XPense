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
import android.widget.Toast;

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

        background = (LinearLayout) getActivity().findViewById(R.id.background);

        background.setPadding(0, 0, 0, 0);

        initAdapter();

        getListView().setDividerHeight(20);

        return;
    }

    private void initAdapter()
    {
        mAdapter = new TripAdapter(getActivity().getApplicationContext());
        setListAdapter(mAdapter);
        Cursor c = db.rawQuery("SELECT * FROM Travel", null);
        while(c.moveToNext()) {

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
                Double sum = c2.getDouble(0);
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
            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1, int arg2, long arg3) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Modify trip?");
                dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Edit the trip
                        // TODO : Edit
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
                        final AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(getActivity());
                        confirmDeleteDialog.setTitle("Are you sure you want to delete this item?");
                        confirmDeleteDialog.setMessage("This action is not reversible.");
                        confirmDeleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO : Delete
                                String Message = "Trip deleted";
                                try {
                                    Trip trip = (Trip) arg1.getTag();
                                    String travelID = trip.getmTravelID();
                                    String query = String.format("DELETE FROM Travel WHERE TravelID = %s;", travelID);
                                    db.execSQL(query);
                                    initAdapter();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                    Message = "Error occurred during deletion";
                                }

                                Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        });
                        confirmDeleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        confirmDeleteDialog.create().show();
                        dialog.cancel();
                    }
                });

                dialog.create().show();

                return true;
            }
        });
    }
}

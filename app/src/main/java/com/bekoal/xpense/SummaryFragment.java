package com.bekoal.xpense;

import android.animation.AnimatorSet;
import android.app.Activity;
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
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListFragment;
import android.widget.Toast;

import com.bekoal.xpense.service.DatabaseHelper;
import com.bekoal.xpense.service.QueryResult;
import com.bekoal.xpense.service.TravelModeCommands;
import com.bekoal.xpense.service.TravelModeService;

import java.util.ArrayList;

public class SummaryFragment extends ListFragment {

    TripAdapter mAdapter;
    ExpenseAdapter expenseAdapter;
    private BroadcastReceiver _reciever = null;
    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private int layout;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();
        mAdapter = new TripAdapter(getActivity().getApplicationContext());
        setListAdapter(mAdapter);

        Cursor c = db.rawQuery("SELECT * FROM Travel", null);
        while(c.moveToNext()) {
            if (c.moveToNext()) {
                String[] args = new String[c.getColumnCount()];
                for (int i = 0; i < c.getColumnCount(); i++) {
                    args[i] = c.getString(i);
                }
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

        expenseAdapter = new ExpenseAdapter(getActivity().getApplicationContext());
        setListAdapter(expenseAdapter);

        Cursor c = db.rawQuery(queryString, null);
        while(c.moveToNext()) {
            if (c.moveToNext()) {
                String[] args = new String[c.getColumnCount()];
                for (int i = 0; i < c.getColumnCount(); i++) {
                    args[i] = c.getString(i);
                }
                Expense newExpense = new Expense(args);
                expenseAdapter.addExpense(newExpense);
            }
        }


//        String toastString = new String();
//
//        Trip currentTrip = (Trip) listView.getItemAtPosition(position);
//        String travelID = currentTrip.getmTravelID();
//        String queryString = "SELECT COUNT(*) FROM Expenses WHERE TravelID = " + travelID;
//        Cursor c = db.rawQuery(queryString, null);
//        while(c.moveToNext()){
//            if(c.moveToNext()){
//                toastString = c.getString(1);
//            }
//        }
//        c.moveToNext();
//        toastString = c.getString(0);
//
//
//       // String toastString = currentTrip.getmTravelID();
//        Toast toast = Toast.makeText(getActivity().getApplicationContext(), toastString, Toast.LENGTH_LONG);
//        toast.show();
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

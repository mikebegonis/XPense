package com.bekoal.xpense;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.app.ListFragment;

import com.bekoal.xpense.service.DatabaseHelper;

public class SummaryFragment extends ListFragment {

    private ExpenseFragment mExpenseFragment;
    OnTravelSelectedListener mCallback;

    TripAdapter mAdapter;
    ExpenseAdapter expenseAdapter;
    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private int layout;


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
}

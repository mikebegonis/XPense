package com.bekoal.xpense;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.app.ListFragment;

public class SummaryFragment extends ListFragment {

    TripAdapter mAdapter;
    private static final String[] TRIPS = { "Sample Trip 1", "Sample Trip 2", "Sample Trip 3" };

//    Tim's code:
//    mAdapter = new TripAdapter(getApplicationContext());
//    setListAdapter(mAdapter);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use different layout definition, depending on whether device is pre-
        // or post-honeycomb

        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
                : android.R.layout.simple_list_item_1;

        // Set the list adapter for this ListFragment
        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, TRIPS));
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

package com.bekoal.xpense;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
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

import com.bekoal.xpense.service.QueryResult;
import com.bekoal.xpense.service.TravelModeCommands;
import com.bekoal.xpense.service.TravelModeService;

import java.util.ArrayList;

public class SummaryFragment extends ListFragment {

    TripAdapter mAdapter;
    private static final String[] TRIPS = { "Sample Trip 1", "Sample Trip 2", "Sample Trip 3" };

    private BroadcastReceiver _reciever = null;
    private int layout;
//    Tim's code:
//    mAdapter = new TripAdapter(getApplicationContext());
//    setListAdapter(mAdapter);


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayList<Parcelable> list = intent.getParcelableArrayListExtra(TravelModeCommands.EXECUTE_QUERY);
                ArrayList<String> stringList = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    QueryResult q = (QueryResult)list.get(i);
                    String str = "";
                    for(int k = 0 ; k < q.getResults().length ; ++k)
                    {
                        str += q.getResults()[k] + ", ";
                    }
                    stringList.add(str);


                }
                setListAdapter(new ArrayAdapter<String>(getActivity(), layout, stringList));

            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(_reciever,
                new IntentFilter(TravelModeCommands.EXECUTE_QUERY));


        Intent intent = new Intent(getActivity(), TravelModeService.class);
        intent.putExtra(TravelModeCommands.EXECUTE_QUERY, "SELECT * FROM Expenses");
        getActivity().startService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // use different layout definition, depending on whether device is pre-
        // or post-honeycomb

        layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? android.R.layout.simple_list_item_activated_1
                : android.R.layout.simple_list_item_1;
//
//        // Set the list adapter for this ListFragment
//        setListAdapter(new ArrayAdapter<String>(getActivity(), layout, TRIPS));
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

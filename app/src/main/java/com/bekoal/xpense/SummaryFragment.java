package com.bekoal.xpense;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.TextView;
import android.app.ListFragment;

import com.bekoal.xpense.service.DatabaseHelper;
import com.bekoal.xpense.service.QueryResult;
import com.bekoal.xpense.service.TravelModeCommands;
import com.bekoal.xpense.service.TravelModeService;

import java.util.ArrayList;
import java.util.Locale;

public class SummaryFragment extends ListFragment {

    TripAdapter mAdapter;
    private static final String[] TRIPS = { "Sample Trip 1", "Sample Trip 2", "Sample Trip 3" };

    private BroadcastReceiver _reciever = null;
    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private int layout;

    ArrayAdapter<String> adapter = null;
//    Tim's code:
//    mAdapter = new TripAdapter(getApplicationContext());
//    setListAdapter(mAdapter);


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM Expenses", null);

        ArrayList<String> strings = new ArrayList<>();
        while(c.moveToNext())
        {
            String str = "";
            for (int i = 0; i < c.getColumnCount(); i++) {
                str += c.getString(i) + " , ";
            }
            strings.add(str);
        }

        c = db.rawQuery("SELECT * FROM Locations", null);

        while(c.moveToNext())
        {
            String str = "";
            for (int i = 0; i < c.getColumnCount(); i++) {
                str += c.getString(i) + " , ";
            }
            strings.add(str);
        }

        adapter = new ArrayAdapter<String>(getActivity(), layout, strings);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = adapter.getItem(position);
                String[] arr = str.split(",");
                if(arr.length <= 6)
                {
                    double lat = Double.parseDouble(arr[0]);
                    double lon = Double.parseDouble(arr[1]);
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getActivity().startActivity(intent);
                }
            }
        });
        setListAdapter(adapter);

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

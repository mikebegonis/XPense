package com.bekoal.xpense;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bekoal.xpense.service.DatabaseHelper;
import com.bekoal.xpense.service.TravelModeService;

public class MainActivity extends ActionBarActivity implements SummaryFragment.OnTravelSelectedListener {

    public static int TAKE_PICTURE = 1;

    private AddFragment mAddFragment;
    private SummaryFragment mSummaryFragment;
    private TravelModeFragment mTravelModeFragment;
    private ExpenseFragment mExpenseFragment;

    private ServiceConnection mConnection = null;
    private TravelModeService travelModeService = null;

    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        dbHelper = new DatabaseHelper(getApplicationContext());
        database = dbHelper.getWritableDatabase();

        mSummaryFragment = new SummaryFragment();
        mAddFragment = new AddFragment();
        mTravelModeFragment = new TravelModeFragment();
        


        // Create buttons
        final Button summaryButton = (Button) findViewById(R.id.summary_button);
        final Button addButton = (Button) findViewById(R.id.add_button);
        final Button travelModeButton = (Button) findViewById(R.id.travel_mode_button);

        summaryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentTransaction summaryTransaction = getFragmentManager().beginTransaction();
                summaryTransaction.replace(R.id.fragment_container,mSummaryFragment);
                summaryTransaction.addToBackStack(null);
                summaryTransaction.commit();
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mSummaryFragment).commit();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mAddFragment).commit();
            }
        });

        travelModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mTravelModeFragment).commit();
                mTravelModeFragment.setInTravelMode(travelModeService.isInTravelMode());
            }
        });

        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                TravelModeService.TravelModeBinder _binder =
                        (TravelModeService.TravelModeBinder)binder;

                travelModeService = _binder.getService();
                Toast.makeText(MainActivity.this, "Connected to background service", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                travelModeService = null;
                Toast.makeText(MainActivity.this, "Disconnected from background service", Toast.LENGTH_LONG).show();
            }
        };

        // Default to the summary fragment
        FragmentManager mFragmentManager = getFragmentManager();

        FragmentTransaction fTransaction = mFragmentManager.beginTransaction();

        Fragment fragment = mSummaryFragment;

        try
        {
            Intent intent = getIntent();

            if(intent.hasExtra("NAME")) {
                Bundle args = new Bundle();
                args.putString("NAME", intent.getStringExtra("NAME"));
                args.putString("ADDRESS", intent.getStringExtra("ADDRESS"));

                mAddFragment.setArguments(args);
                fragment = mAddFragment;
                ((RadioButton)addButton).toggle();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        fTransaction.add(R.id.fragment_container, fragment);
//        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }

    @Override
    public void onTripSelected(String query){
        mExpenseFragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ExpenseFragment.queryString, query);
        mExpenseFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mExpenseFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, TravelModeService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public TravelModeService getTravelModeService() {
        return travelModeService;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }
}
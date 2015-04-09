package com.bekoal.xpense;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private AddFragment mAddFragment;
    private SummaryFragment mSummaryFragment;
    private TravelModeFragment mTravelModeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mSummaryFragment = new SummaryFragment();
        mAddFragment = new AddFragment();
        mTravelModeFragment = new TravelModeFragment();

        // Default to the summary fragment
        FragmentManager mFragmentManager = getFragmentManager();

        FragmentTransaction fTransaction = mFragmentManager.beginTransaction();
        fTransaction.add(R.id.fragment_container, mSummaryFragment);
        fTransaction.commit();

        // Create buttons
        final Button summaryButton = (Button) findViewById(R.id.summary_button);
        final Button addButton = (Button) findViewById(R.id.add_button);
        final Button travelModeButton = (Button) findViewById(R.id.travel_mode_button);

        RadioGroup group = (RadioGroup) findViewById(R.id.radio_group);
        group.check(R.id.summary_button);

        summaryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mSummaryFragment).commit();
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
            }
        });
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

    // When a user interacts with the checkbox:
    public void onCheckBoxClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.summary_checkbox) {
            if (isChecked) {
                Toast travelToastCheck = Toast.makeText(getApplicationContext(),
                        "This did absolutely nothing.... for now....",
                        Toast.LENGTH_LONG);
                travelToastCheck.show();
            } else {
                Toast travelToastUncheck = Toast.makeText(getApplicationContext(),
                        "You just undid absolutely nothing... for now ...",
                        Toast.LENGTH_LONG);
                travelToastUncheck.show();
            }
        }
    }
}

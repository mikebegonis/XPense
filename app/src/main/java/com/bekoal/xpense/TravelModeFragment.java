package com.bekoal.xpense;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.util.Log;
import android.widget.LinearLayout;

import com.bekoal.xpense.service.DatabaseHelper;
import com.bekoal.xpense.service.TravelModeService;

public class TravelModeFragment extends Fragment {

    CheckBox mTravelModeCheckbox = null;

    private boolean isInTravelMode = false;

    private LinearLayout background;
    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.travelmode_fragment, container, false);

        SharedPreferences prefs = this.getActivity().getSharedPreferences("com.bekoal.xpense",
                Context.MODE_PRIVATE);

        isInTravelMode = prefs.getBoolean(TravelModeService.IS_TRAVEL_MODE_ACTIVE, false);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getWritableDatabase();

        background = (LinearLayout) getActivity().findViewById(R.id.background);
        background.setPadding(16, 16, 16, 16);

        mTravelModeCheckbox = (CheckBox)view.findViewById(R.id.travel_mode_checkbox);
        mTravelModeCheckbox.setChecked(isInTravelMode);

        // TODO : implement actual toggling of TravelModeService
        mTravelModeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelModeService service = ((MainActivity)getActivity()).getTravelModeService();
                if(mTravelModeCheckbox.isChecked()) {
                    service.EnableTravelMode();
                    isInTravelMode = true;
                }
                else {
                    service.DisableTravelMode();
                    isInTravelMode = false;
                }


            }
        });

        view.findViewById(R.id.travel_mode_button_clear_location_table)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.execSQL("DELETE FROM Locations");
                    }
                });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



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

    public boolean isInTravelMode() {
        return isInTravelMode;
    }

    public void setInTravelMode(boolean isInTravelMode) {
        this.isInTravelMode = isInTravelMode;
//        mTravelModeCheckbox.setChecked(isInTravelMode);
    }
}

package com.bekoal.xpense.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TravelModeService extends Service {

//    private TravelModeBinder _binder = null;

    public static final String IS_TRAVEL_MODE_ACTIVE = "IS_TRAVEL_MODE_ACTIVE";

    private SQLiteDatabase database = null;
    private final IBinder mBinder = new TravelModeBinder();
    private boolean isInTravelMode = false;
    private LocationManager locationManager = null;
    private LocationListener listener = null;

    private final long MIN_TIME = 1000 * 15;
    private final long MIN_DISTANCE = 0;

    private final String insertLocationQueryFormat
            = "INSERT INTO Locations VALUES(%f, %f, '%s');";


    private DatabaseHelper dbHandler = null;

    TravelModeReceiver alarm = new TravelModeReceiver();

    public TravelModeService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();



        dbHandler = new DatabaseHelper(getApplicationContext());
        database = dbHandler.getWritableDatabase();
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);



        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Calendar calendar = Calendar.getInstance();
                String time = SimpleDateFormat.getDateTimeInstance().format(calendar.getTime());

                database.execSQL(String.format(insertLocationQueryFormat,
                        lat, lon, time));




            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        SharedPreferences prefs = this.getSharedPreferences("com.bekoal.xpense", Context.MODE_PRIVATE);

        isInTravelMode = prefs.getBoolean(IS_TRAVEL_MODE_ACTIVE, false);
        if(isInTravelMode)
            EnableTravelMode();
        else
            DisableTravelMode();

    }

    public void EnableTravelMode()
    {
        isInTravelMode = true;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME, MIN_DISTANCE, listener);
        SharedPreferences prefs = this.getSharedPreferences("com.bekoal.xpense", Context.MODE_PRIVATE);
        prefs.edit().putBoolean(IS_TRAVEL_MODE_ACTIVE, isInTravelMode).commit();
    }

    public void DisableTravelMode()
    {
        isInTravelMode = false;
        locationManager.removeUpdates(listener);
        SharedPreferences prefs = this.getSharedPreferences("com.bekoal.xpense", Context.MODE_PRIVATE);
        prefs.edit().putBoolean(IS_TRAVEL_MODE_ACTIVE, isInTravelMode).commit();
    }

    public boolean isInTravelMode() {
        return isInTravelMode;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);


        alarm.SetAlarm(TravelModeService.this);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    protected void onHandleIntent(Intent intent) {
        // get data from intent
        // handle data

        // TODO : Gracefully remove

        String data = null;

        if((data = intent.getStringExtra(TravelModeCommands.TEST_CONNECTION)) != null) {
            data += "Message Received from service.  And sent back!";

            Intent response = new Intent(TravelModeCommands.TEST_CONNECTION);
            response.putExtra(TravelModeCommands.TEST_CONNECTION, data);

            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
        }
        else if((data = intent.getStringExtra(TravelModeCommands.EXECUTE_QUERY)) != null)
        {
            Cursor c = database.rawQuery(data, null);

            ArrayList<QueryResult> list = new ArrayList<QueryResult>();

            while(c.moveToNext())
            {
                String[] arr = new String[c.getColumnCount()];
                for(int i = 0 ; i < c.getColumnCount() ; ++i)
                {
                    arr[i] = c.getString(i);
                }
                list.add(new QueryResult(arr));
            }

            c.close();

            Intent response = new Intent(TravelModeCommands.EXECUTE_QUERY);
            response.putExtra(TravelModeCommands.EXECUTE_QUERY, data);

            response.putParcelableArrayListExtra(TravelModeCommands.EXECUTE_QUERY, list);


            LocalBroadcastManager.getInstance(this).sendBroadcast(response);
        }
        else if((data = intent.getStringExtra(TravelModeCommands.EXECUTE_INSERT)) != null)
        {
            database.execSQL(data);
        }

    }

    public class TravelModeBinder extends Binder{
        public TravelModeService getService(){
            return TravelModeService.this;
        }


    }



}








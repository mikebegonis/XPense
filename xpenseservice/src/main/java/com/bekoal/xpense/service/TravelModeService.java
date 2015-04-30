package com.bekoal.xpense.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TravelModeService extends Service
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
            = "INSERT INTO Locations VALUES(%f, %f, '%s', %d, %d);";






    public static GoogleApiClient mGoogleApiClient = null;






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
                Log.w("TRAVELMODESERVICE", String.format("New Location: %f , %f", lat, lon));

                if(GoogleActivityIntentService._lastKnownActivityState != DetectedActivity.IN_VEHICLE) {
                    if (TravelModeGeofenceIntentService.mGeofence == null) {
                        Log.w("TRAVELMODESERVICE", "Creating new Geofence");
                        TravelModeGeofenceIntentService.mLatGeofence = lat;
                        TravelModeGeofenceIntentService.mLonGeofence = lon;
                        List<Geofence> mGeofenceList = new ArrayList<Geofence>();
                        TravelModeGeofenceIntentService.mGeofence = new Geofence.Builder()
                                .setRequestId(TravelModeGeofenceIntentService.KEY)
                                .setCircularRegion(lat, lon, TravelModeGeofenceIntentService.GEOFENCE_RADIUS)
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
//                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                                .setLoiteringDelay(1000)
                                .build();

                        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
                        builder.setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL);
//                        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
//                                GeofencingRequest.INITIAL_TRIGGER_ENTER |
//                                GeofencingRequest.INITIAL_TRIGGER_EXIT);

                        builder.addGeofence(TravelModeGeofenceIntentService.mGeofence);


                        mGeofenceList.add(TravelModeGeofenceIntentService.mGeofence);

                        Intent intent = new Intent(getApplicationContext(), TravelModeGeofenceIntentService.class);
//                        Intent intent = new Intent(TravelModeService.this, TravelModeGeofenceReceiver.class);
                        TravelModeGeofenceIntentService.mGeofencePI = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                        PendingIntent pendingIntent = PendingIntent.getBroadcast(TravelModeService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient,
                                builder.build(),
                                TravelModeGeofenceIntentService.mGeofencePI)
                                .setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
//                                        String str = status.getStatusMessage();
//                                        Log.i("TRAVEL_MODE_SERVICE", str);
                                    }
                                });
                    }
                }
                else // if we're in a vehicle
                {
                    if(TravelModeGeofenceIntentService.mGeofence != null)
                        TravelModeGeofenceIntentService.CancelGeofence();
                }

                database.execSQL(String.format(insertLocationQueryFormat,
                        lat, lon, time, GoogleActivityIntentService._lastKnownActivityState, GoogleActivityIntentService._lastKnownActivityStateConfidence));

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



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();






        SharedPreferences prefs = this.getSharedPreferences("com.bekoal.xpense", Context.MODE_PRIVATE);

        isInTravelMode = prefs.getBoolean(IS_TRAVEL_MODE_ACTIVE, false);
        if(isInTravelMode)
            EnableTravelMode();
        else
            DisableTravelMode();

    }


    public static class GoogleActivityIntentService extends IntentService
    {

        private static int _lastKnownActivityState = DetectedActivity.UNKNOWN;
        private static int _lastKnownActivityStateConfidence = -1;
        public GoogleActivityIntentService() {
            super("Travel Mode Google Activity Service");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            if(ActivityRecognitionResult.hasResult(intent)) {
                ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
                DetectedActivity detectedActivity = result.getMostProbableActivity();
//                _lastKnownActivityState = detectedActivity.getType();
//                _lastKnownActivityStateConfidence = detectedActivity.getConfidence();

                if(detectedActivity.getType() == DetectedActivity.UNKNOWN
                        && detectedActivity.getConfidence() > 80)
                {
                    _lastKnownActivityState = detectedActivity.getType();
                    _lastKnownActivityStateConfidence = detectedActivity.getConfidence();
                }
                if(detectedActivity.getConfidence() > 60)
                {
                    _lastKnownActivityState = detectedActivity.getType();
                    _lastKnownActivityStateConfidence = detectedActivity.getConfidence();
                }



            }
        }

        public static String DetectedActivityToString(int value)
        {
            String str = "";
            switch(value)
            {
                case 0:
                    str = "IN_VEHICLE";
                    break;
                case 1:
                    str = "ON_BICYCLE";
                    break;
                case 2:
                    str = "ON_FOOT";
                    break;
                case 8:
                    str = "RUNNING";
                    break;
                case 3:
                    str = "STILL";
                    break;
                case 5:
                    str = "TILTING";
                    break;
                case 4:
                    str = "UNKNOWN";
                    break;
                case 7:
                    str = "WALKING";
                    break;

            }

            return str;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Intent intent = new Intent(this, GoogleActivityReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Intent intent = new Intent(this, GoogleActivityIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mGoogleApiClient, 1000, pendingIntent);


//        pendingResult.setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(Status status) {
//                Toast.makeText(TravelModeService.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });


    }

    @Override
    public void onConnectionSuspended(int i) {
//        Intent intent = new Intent(this, GoogleActivityReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Intent intent = new Intent(this, GoogleActivityIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(mGoogleApiClient, pendingIntent);
        GoogleActivityIntentService._lastKnownActivityState = DetectedActivity.UNKNOWN;
        GoogleActivityIntentService._lastKnownActivityStateConfidence = -1;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void EnableTravelMode()
    {
        isInTravelMode = true;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_TIME, MIN_DISTANCE, listener);
        SharedPreferences prefs = this.getSharedPreferences("com.bekoal.xpense", Context.MODE_PRIVATE);
        prefs.edit().putBoolean(IS_TRAVEL_MODE_ACTIVE, isInTravelMode).commit();

        String str = "Google Play Services is NOT available";
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)
            str = "Google Play Services is available";
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        mGoogleApiClient.connect();
    }

    public void DisableTravelMode()
    {
        isInTravelMode = false;
        locationManager.removeUpdates(listener);
        TravelModeGeofenceIntentService.CancelGeofence();

        SharedPreferences prefs = this.getSharedPreferences("com.bekoal.xpense", Context.MODE_PRIVATE);
        prefs.edit().putBoolean(IS_TRAVEL_MODE_ACTIVE, isInTravelMode).commit();

        mGoogleApiClient.disconnect();
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



    public class TravelModeBinder extends Binder{
        public TravelModeService getService(){
            return TravelModeService.this;
        }


    }



}








package com.bekoal.xpense.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

public class TravelModeGeofenceIntentService extends IntentService
{
    public static final String KEY = "TRAVEL_MODE_GEOFENCE_KEY";

    //public static long TRIGGER_TIME = 60 * 1000;
    public static long TRIGGER_TIME = 5 * 1000;

    // in meters
    public static final float GEOFENCE_RADIUS = 1000f;

    private static Calendar mTimeDifference;

    private static boolean TRIGGER_HIT = false;

    public static PendingIntent mGeofencePI = null;
    public static double mLatGeofence = 0;
    public static double mLonGeofence = 0;
    public static Geofence mGeofence = null;


    public TravelModeGeofenceIntentService() {
        super("Travel Mode Geofence Service");
//        mTimeDifference = Calendar.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        if(!event.hasError())
        {
            Geofence geofence = event.getTriggeringGeofences().get(0);

            switch(event.getGeofenceTransition()) {
                case Geofence.GEOFENCE_TRANSITION_DWELL:

                    if (!TRIGGER_HIT) {
                        // First getTime() returns a Date, second returns a long
                        long diff = Calendar.getInstance().getTime().getTime() - mTimeDifference.getTime().getTime();

                        if (diff > TRIGGER_TIME) {
                            TRIGGER_HIT = true;
                            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, 10);

                            Intent placeIntent = new Intent(this, PlaceDetectionReceiver.class);
                            placeIntent.putExtra(PlaceDetectionReceiver.LATITUDE, mLatGeofence);
                            placeIntent.putExtra(PlaceDetectionReceiver.LONGITUDE, mLonGeofence);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, placeIntent, 0);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            CancelGeofence();
                        }
                    }

                    break;

                case Geofence.GEOFENCE_TRANSITION_EXIT:
                    CancelGeofence();

                    break;

                case Geofence.GEOFENCE_TRANSITION_ENTER:
                    mTimeDifference = Calendar.getInstance();
                    TRIGGER_HIT = false;
                    break;
            }
        }
        else
        {
//                Log.e("TRAVEL_MODE_SERVICE", GeoFenceErrorMessages.getErrorString(this, event.getErrorCode()));
            Log.e("TRAVEL_MODE_SERVICE", String.valueOf(event.getErrorCode()));
        }
    }

    public static void CancelGeofence()
    {
        if(mGeofencePI != null) {
            LocationServices.GeofencingApi.removeGeofences(TravelModeService.mGoogleApiClient,
                    mGeofencePI).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {

                }
            });
        }

        mGeofence = null;

    }


}

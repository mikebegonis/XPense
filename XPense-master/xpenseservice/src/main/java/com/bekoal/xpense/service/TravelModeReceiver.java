package com.bekoal.xpense.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by begon_000 on 4/14/2015.
 */
public class TravelModeReceiver  extends BroadcastReceiver {

    private static final long REPEAT_TIME = 1000 * 30;

    private static final long QUERY_LOCATION_INTERVAL = 1000 * 15;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager service = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, StartTravelModeReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar calendar = Calendar.getInstance();

        // Start 30 seconds after boot completed
        calendar.add(Calendar.SECOND, 30);
        // fetch every 30 seconds

        // InexactRepeating allows Android to optimize the energy consumption
        service.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                REPEAT_TIME, pending);

    }

    public void SetAlarm(Context context)
    {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TravelModeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                QUERY_LOCATION_INTERVAL, pendingIntent);
    }

    public  void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, TravelModeReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }


}

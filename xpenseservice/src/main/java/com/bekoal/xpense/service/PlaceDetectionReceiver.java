package com.bekoal.xpense.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Locale;

/**
 * Created by begon_000 on 4/27/2015.
 */
public class PlaceDetectionReceiver extends BroadcastReceiver{

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";


    @Override
    public void onReceive(Context context, Intent intent) {


        double lat = intent.getDoubleExtra(LATITUDE, 0);
        double lon = intent.getDoubleExtra(LONGITUDE, 0);

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);

        Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                geoIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification.Builder mBuilder =
                new Notification.Builder(context)
                .setAutoCancel(true)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentIntent(pendingIntent)
                .setContentTitle("You been hanging out here for a while huh?")
                .setContentText(String.format("%f,%f", lat, lon));


        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(696969, mBuilder.build());


    }
}

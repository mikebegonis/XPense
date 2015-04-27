package com.bekoal.xpense.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.location.places.Places;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * Created by begon_000 on 4/27/2015.
 */
public class PlaceDetectionReceiver extends BroadcastReceiver{

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";


    @Override
    public void onReceive(Context context, Intent intent) {

        final Context mContext = context;
        final double lat = intent.getDoubleExtra(LATITUDE, 0);
        final double lon = intent.getDoubleExtra(LONGITUDE, 0);

//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lon);

//        Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                geoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Places.GeoDataApi.getPlaceById()
        Places.PlaceDetectionApi.getCurrentPlace(TravelModeService.mGoogleApiClient, null)
                .setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                        NotificationManager manager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        int id = 379783;
                        for(PlaceLikelihood placeLikelihood : likelyPlaces)
                        {
                            Place place = placeLikelihood.getPlace();
                            if(place.getPlaceTypes().contains(Place.TYPE_FOOD))
                            {
                                String address = place.getAddress().toString();
                                String name = place.getName().toString();
                                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + address));

                                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                                        geoIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                                Notification.Builder mBuilder =
                                        new Notification.Builder(mContext)
                                                .setAutoCancel(true)
                                                .setSmallIcon(android.R.drawable.stat_sys_warning)
                                                .setContentIntent(pendingIntent)
                                                .setContentTitle(name)
                                                .setContentText(address);



                                manager.notify(id++, mBuilder.build());
                            }
                        }


                    }
                });



//        Notification.Builder mBuilder =
//                new Notification.Builder(context)
//                .setAutoCancel(true)
//                        .setSmallIcon(android.R.drawable.stat_sys_warning)
//                .setContentIntent(pendingIntent)
//                .setContentTitle("You been hanging out here for a while huh?")
//                .setContentText(String.format("%f,%f", lat, lon));
//
//
//        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(696969, mBuilder.build());


    }
}

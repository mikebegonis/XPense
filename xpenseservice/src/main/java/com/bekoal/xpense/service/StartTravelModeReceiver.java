package com.bekoal.xpense.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by begon_000 on 4/14/2015.
 */
public class StartTravelModeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, TravelModeService.class);
        context.startService(service);
    }
}

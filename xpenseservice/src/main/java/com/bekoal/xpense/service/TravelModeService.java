package com.bekoal.xpense.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;

public class TravelModeService extends IntentService {

    public TravelModeService() {
        super("TravelModeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from intent
        // handle data
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class TravelModeBinder extends Binder
    {
        public TravelModeService getService()
        {
            return TravelModeService.this;
        }
    }
}

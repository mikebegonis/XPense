package com.bekoal.xpense.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class TravelModeService extends IntentService {

//    private TravelModeBinder _binder = null;

    public TravelModeService() {
        super("TravelModeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from intent
        // handle data

        String data = intent.getStringExtra(TravelModeCommands.TEST_CONNECTION);
        data += "Message Received from service.  And sent back!";

        Intent response = new Intent(TravelModeCommands.TEST_CONNECTION);
        response.putExtra(TravelModeCommands.TEST_CONNECTION, data);

        LocalBroadcastManager.getInstance(this).sendBroadcast(response);

    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        //return super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
//    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        return (_binder = new TravelModeBinder());
//    }
//
//    public class TravelModeBinder extends Binder
//    {
//        public TravelModeService getService()
//        {
//            return TravelModeService.this;
//        }
//    }


}

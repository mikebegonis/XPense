package com.bekoal.xpense.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

public class TravelModeService extends IntentService {

//    private TravelModeBinder _binder = null;

    private SQLiteDatabase database = null;




    private DatabaseHelper dbHandler = null;

    public TravelModeService() {
        super("TravelModeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();



        dbHandler = new DatabaseHelper(getApplicationContext());
        database = dbHandler.getWritableDatabase();


//        database = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null, Context.MODE_PRIVATE);
//        database.execSQL(CREATE_TABLE_EXPENSES);
//        database.execSQL(INSERT_EXPENSE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get data from intent
        // handle data

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


//            c.moveToNext();
//            data = c.getString(c.getColumnIndex("Date")) + "   ";
//            data += "$" + c.getDouble(c.getColumnIndex("Amount")) + "    ";
//            data += c.getString(c.getColumnIndex("Description"));



//            data = c.getString(0) + "   ";
//            data += "$" + c.getDouble(1) + "    ";
//            data += c.getString(2);

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








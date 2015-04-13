package com.bekoal.xpense.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TravelModeService extends IntentService {

//    private TravelModeBinder _binder = null;

    private SQLiteDatabase database = null;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "XDatabase";


    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE IF NOT EXISTS Expenses (" +
            "`Date` TEXT NOT NULL, " +
            "`Amount` REAL NOT NULL, " +
            "`Description` TEXT, " +
            "`Img` BLOB, " +
            "`Location` TEXT, " +
            "`TravelID` INTEGER, " +
            "`ExpenseID` INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`DestinationID` INTEGER, " +
            "`Type` TEXT " +
            ");";



    private DatabaseHandler dbHandler = null;

    public TravelModeService() {
        super("TravelModeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();



        dbHandler = new DatabaseHandler(getApplicationContext());
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

//    private static class DataBaseHelper extends SQLiteOpenHelper {
//        private Context mycontext;
//
//        private String DB_PATH = "";
//        private static String DB_NAME = DATABASE_NAME; //"(datbasename).sqlite";//the extension may be .sqlite or .db
//        public SQLiteDatabase myDataBase;
//    /*private String DB_PATH = "/data/data/"
//                        + mycontext.getApplicationContext().getPackageName()
//                        + "/databases/";*/
//
//        public DataBaseHelper(Context context) throws IOException {
//            super(context,DB_NAME,null,1);
//            this.mycontext=context;
//            DB_PATH = mycontext.getApplicationContext().getPackageName()+"/databases/";
//            boolean dbexist = checkdatabase();
//            if (dbexist) {
//                //System.out.println("Database exists");
//                opendatabase();
//            } else {
//                System.out.println("Database doesn't exist");
//                createdatabase();
//            }
//        }
//
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        }
//
//        public void createdatabase() throws IOException {
//            boolean dbexist = checkdatabase();
//            if(dbexist) {
//                //System.out.println(" Database exists.");
//            } else {
//                this.getReadableDatabase();
//                try {
//                    copydatabase();
//                } catch(IOException e) {
//                    throw new Error("Error copying database");
//                }
//            }
//        }
//
//        private boolean checkdatabase() {
//            //SQLiteDatabase checkdb = null;
//            boolean checkdb = false;
//            try {
//                String myPath = DB_PATH + DB_NAME;
//                File dbfile = new File(myPath);
//                //checkdb = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
//                checkdb = dbfile.exists();
//            } catch(SQLiteException e) {
//                System.out.println("Database doesn't exist");
//            }
//            return checkdb;
//        }
//
//        private void copydatabase() throws IOException {
//            //Open your local db as the input stream
//            InputStream myinput = mycontext.getAssets().open(DB_NAME);
//
//            // Path to the just created empty db
//            String outfilename = DB_PATH + DB_NAME;
//
//            //Open the empty db as the output stream
//            OutputStream myoutput = new FileOutputStream("/data/data/(packagename)/databases   /(datbasename).sqlite");
//
//            // transfer byte to inputfile to outputfile
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = myinput.read(buffer))>0) {
//                myoutput.write(buffer,0,length);
//            }
//
//            //Close the streams
//            myoutput.flush();
//            myoutput.close();
//            myinput.close();
//        }
//
//        public void opendatabase() throws SQLException {
//            //Open the database
//            String mypath = DB_PATH + DB_NAME;
//            myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
//        }
//
//        public synchronized void close() {
//            if(myDataBase != null) {
//                myDataBase.close();
//            }
//            super.close();
//        }
//
//    }

    private static class DatabaseHandler extends SQLiteOpenHelper {

        private  static final String TABLE_EXPENSES = "Expenses";

        public DatabaseHandler(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_EXPENSES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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








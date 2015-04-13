package com.bekoal.xpense.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private  static final String TABLE_EXPENSES = "Expenses";

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

    public DatabaseHelper(Context context)
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
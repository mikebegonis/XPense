package com.bekoal.xpense;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bekoal.xpense.service.DatabaseHelper;

/**
 * Created by Tim on 4/7/2015.
 */
public class Trip extends DatabaseItem {

    public enum Status {NOT_DONE, DONE};

    // Private values for Trip class

    private String mTitle = new String();
    private Status mStatus = Status.NOT_DONE;
    private Date mStartDate = new Date();
    private Date mEndDate = new Date();
    private String mNote = new String();
    private String mTravelID = new String();
    private int mTotal = 0;

    public Trip(String[] args){
        super("TravelID", "Travel");

        try{
            if (args[0] != null)
                mStartDate = new SimpleDateFormat("MM-dd-yyyy").parse(args[0]);
            if (args[1] != null)
                mEndDate = new SimpleDateFormat("MM-dd-yyyy").parse(args[1]);
        }
        catch (ParseException e) {

        }

        if(args[2] != null)
            mTitle = args[2];

        if(args[3] == "DONE"){
            mStatus = Status.DONE;
        }
        else if(args[3] == "NOT_DONE"){
            mStatus = Status.NOT_DONE;
        }
        if(args[4] != null)
            mNote = args[4];

        if(args[5] != null)
            mTravelID = args[5];
    }

//    Constructor for Trip object, from explicitly passed data

    public Trip(Date startDate, Date endDate, String title, Status status, String travelID){
        super("TravelID", "Travel");
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mTitle = title;
        this.mStatus = status;
        this.mTravelID = travelID;
    }

    public static ArrayList<Trip> QueryDatabase(String query, Context context)
    {
        ArrayList<Trip> items = new ArrayList<Trip>();

        SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();

        Cursor c = database.rawQuery(query, null);

        while(c.moveToNext())
        {
            String[] arr = new String[c.getColumnCount()];
            for(int i = 0 ; i < c.getColumnCount() ; ++i)
            {
                arr[i] = c.getString(i);
            }
            items.add(new Trip(arr));
        }

        c.close();
        database.close();

        return items;
    }

    protected String getPrimaryKeyValue() {
        return mTravelID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Status getmStatus() {
        return mStatus;
    }

    public void setmStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    public Date getmStartDate() {
        return mStartDate;
    }

    public void setmStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Date getmEndDate() {
        return mEndDate;
    }

    public void setmEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public int getmTotal() {return mTotal; }

    public void setmTotal(int mTotal) {this.mTotal = mTotal; }

    public String getmNote() { return mNote; }

    public void setmNote(String mNote) { this.mNote = mNote; }

    public String getmTravelID(){ return mTravelID; }

    public void setmTravelID(String mTravelID) { this.mTravelID = mTravelID; }

}


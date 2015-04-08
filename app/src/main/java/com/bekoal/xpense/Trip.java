package com.bekoal.xpense;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Tim on 4/7/2015.
 */
public class Trip {


    public enum Status {NOT_DONE, DONE};

    // Public values for packaging
    public final static SimpleDateFormat DATE_FORMAT  = new SimpleDateFormat("MM:dd:yyyy", Locale.US);
    public final static String TITLE = null;
    public final static String STATUS = null;
    public final static String START_DATE = null;
    public final static String END_DATE = null;
    public final static int TOTAL = 0;

    // Private values for Trip class

    private String mTitle = new String();
    private Status mStatus = Status.NOT_DONE;
    private Date mStartDate = new Date();
    private Date mEndDate = new Date();
    private int mTotal = 0;

    // Constructor for Trip object, from explicitly passed data

    Trip(String title, Status status, Date startDate, Date endDate, int total){
        this.mTitle = title;
        this.mStatus = status;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mTotal = total;

    }

    // Constructor for Trip object, from packaged intent data

    Trip(Intent intent) {
        mTitle = intent.getStringExtra(Trip.TITLE);
        mStatus = Status.valueOf(intent.getStringExtra(Trip.STATUS));
        mTotal = intent.getStringExtra(Trip.TOTAL);
        try {
            mStartDate = Trip.DATE_FORMAT.parse(intent.getStringExtra(Trip.START_DATE));
            mEndDate = Trip.DATE_FORMAT.parse(intent.getStringExtra(Trip.END_DATE));
        } catch (ParseException e) {
            mStartDate = new Date();
            mEndDate = new Date();
        }
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


}


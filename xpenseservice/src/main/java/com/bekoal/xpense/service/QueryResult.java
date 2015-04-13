package com.bekoal.xpense.service;

import android.os.Parcel;
import android.os.Parcelable;

public class QueryResult implements Parcelable
{
    String[] Results = null;

    public QueryResult(String[] results) {
        Results = results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(Results);
    }

    public String[] getResults() {
        return Results;
    }
}

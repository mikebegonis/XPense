package com.bekoal.xpense;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.renderscript.Sampler;

import com.bekoal.xpense.service.DatabaseHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by begon_000 on 4/8/2015.
 */
public class Expense extends DatabaseItem {

    private Date date = null;
    private String imagePath = "";
    private Bitmap image = null;
    private double amount = 0.0;
    private String description = "";
    private String location = "";
    private int travelID = -1;
    private int expenseID = -1;
    private int destinationID = -1;

    private ExpenseType type = ExpenseType.Other;

//    public Expense(Date date, String imagePath, Bitmap image, double amount, String description, String location, ExpenseType type) {
//        super("ExpenseID", "Expenses");
//        this.date = date;
//        this.imagePath = imagePath;
//        this.image = image;
//        this.amount = amount;
//        this.description = description;
//        this.location = location;
//        this.type = type;
//    }

    public Expense(String date, String imagePath, Bitmap image, double amount, String description, String location, ExpenseType type) {
        super("ExpenseID", "Expenses");
        try {
            // TODO make sure format here is correct
            this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
        }catch(Exception e){}
        this.imagePath = imagePath;
        this.image = image;
        this.amount = amount;
        this.description = description;
        this.location = location;
        this.type = type;
    }

    public Expense(String[] args)
    {
        super("ExpenseID", "Expenses");
        try {
            // TODO make sure format here is correct
            if(args[0] != null)
                date = new SimpleDateFormat("MM-dd-yyyy").parse(args[0]);
        }
        catch(ParseException e)
        {

        }
//        if(args[1] != null)
//            imagePath = args[1];
        if(args[1] != null)
            amount = Double.parseDouble(args[1]);
        if(args[2] != null)
            description = args[2];
//        if(args[4] != null)
//            location = args[4];
//        if(args[5] != null)
//            travelID = Integer.parseInt(args[5]);
//        if(args[6] != null)
//            expenseID = Integer.parseInt(args[6]);
        if(args[7] != null)
            location = args[7];
        if(args[8] != null)
            type = ExpenseType.valueOf(args[8]);
    }



    public static ArrayList<Expense> QueryDatabase(String query, Context context)
    {
        ArrayList<Expense> items = new ArrayList<Expense>();

        SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();

        Cursor c = database.rawQuery(query, null);

        while(c.moveToNext())
        {
            String[] arr = new String[c.getColumnCount()];
            for(int i = 0 ; i < c.getColumnCount() ; ++i)
            {
                arr[i] = c.getString(i);
            }
            items.add(new Expense(arr));
        }

        c.close();

        database.close();

        return items;
    }




    public String ToQueryString()
    {
        StringBuilder builder = new StringBuilder();

        if(expenseID != -1) {
            builder.append("INSERT INTO Expenses ");
        }
        else {
            builder.append("UPDATE Expenses SET ");



            builder.append(" WHERE expenseID = " + expenseID);
        }



        return builder.toString();
    }


    @Override
    protected String getPrimaryKeyValue() {
        return Integer.toString(expenseID);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        // TODO make sure format here is correct
        ValueChanged("Date", date.toString());
    }

    public Bitmap getImage() {

        if(image == null)
        {
            // TODO : load image from imagePath if hasn't been already
        }


        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
        ValueChanged("Amount", amount);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        ValueChanged("Description", description);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        ValueChanged("Location", location);
    }

    public int getTravelID() {
        return travelID;
    }

    public void setTravelID(int travelID) {
        this.travelID = travelID;
        ValueChanged("TravelID", travelID);
    }

    public int getExpenseID() {
        return expenseID;
    }

    public int getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(int destinationID) {
        this.destinationID = destinationID;
        ValueChanged("DestinationID", destinationID);
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
        ValueChanged("Type", type.toString());
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        ValueChanged("ImagePath", imagePath);
    }
}


package com.bekoal.xpense;

import android.graphics.Bitmap;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by begon_000 on 4/8/2015.
 */
public class Expense {

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

    public Expense(Date date, String imagePath, Bitmap image, double amount, String description, String location, ExpenseType type) {
        this.date = date;
        this.imagePath = imagePath;
        this.image = image;
        this.amount = amount;
        this.description = description;
        this.location = location;
        this.type = type;
    }

    public Expense(String date, String imagePath, Bitmap image, double amount, String description, String location, ExpenseType type) {
        try {
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
        try {
            if(args[0] != null)
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(args[0]);
        }
        catch(ParseException e)
        {

        }
        if(args[1] != null)
            imagePath = args[1];
        if(args[2] != null)
            amount = Double.parseDouble(args[2]);
        if(args[3] != null)
            description = args[3];
        if(args[4] != null)
            location = args[4];
        if(args[5] != null)
            travelID = Integer.parseInt(args[5]);
        if(args[6] != null)
            expenseID = Integer.parseInt(args[6]);
        if(args[7] != null)
            destinationID = Integer.parseInt(args[7]);
        if(args[8] != null)
            type = ExpenseType.valueOf(args[8]);
    }








    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTravelID() {
        return travelID;
    }

    public void setTravelID(int travelID) {
        this.travelID = travelID;
    }

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public int getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(int destinationID) {
        this.destinationID = destinationID;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}


package com.bekoal.xpense;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by begon_000 on 4/13/2015.
 */
public abstract class DatabaseItem {

    private String primaryKey = "";
    private String tableName = "";


    protected DatabaseItem(String primaryKey, String tableName) {
        this.primaryKey = primaryKey;
        this.tableName = tableName;
    }

    HashMap<String, String> map = new HashMap<>();

    protected void ValueChanged(String property, String newValue)
    {
        AddToMap(property, "'" + newValue + "'");
    }

    protected void ValueChanged(String property, int newValue)
    {
        AddToMap(property, Integer.toString(newValue));
    }


    protected void ValueChanged(String property, double newValue)
    {
        AddToMap(property, Double.toString(newValue));
    }

    private void AddToMap(String key, String value)
    {
        if(map.containsKey(key))
            map.remove(key);

        map.put(key, value);

    }

    protected abstract String getPrimaryKeyValue();


    public String getUpdateQueryString()
    {
        StringBuilder builder = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        builder.append("UPDATE " + tableName + "SET ");

        for(Map.Entry<String, String> entry : map.entrySet())
        {
            builder2.append(", " + entry.getKey() + " = " + entry.getValue());
        }
        builder.append(builder2.toString().substring(1));

        builder.append("WHERE " + primaryKey + " = " + getPrimaryKeyValue());

        return builder.toString();
    }






}

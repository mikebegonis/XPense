package com.bekoal.xpense;

import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.app.ListFragment;

import com.bekoal.xpense.service.DatabaseHelper;
/**
 * Created by Tim on 4/28/2015.
 */
public class ExpenseFragment extends ListFragment {
    ExpenseAdapter mAdapter;
    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;
    private ExpenseAdapter expenseAdapter = null;
    public static String queryString;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();

        expenseAdapter = new ExpenseAdapter(getActivity().getApplicationContext());
        setListAdapter(expenseAdapter);

        Bundle bundleArgs = getArguments();

        String newString = new String();
        newString = bundleArgs.getString(queryString);

        Cursor c = db.rawQuery(newString, null);
        while(c.moveToNext()) {
            if (c.moveToNext()) {
                String[] args = new String[c.getColumnCount()];
                for (int i = 0; i < c.getColumnCount(); i++) {
                    args[i] = c.getString(i);
                }
                Expense newExpense = new Expense(args);
                expenseAdapter.addExpense(newExpense);
            }
        }
        return;
    }
}

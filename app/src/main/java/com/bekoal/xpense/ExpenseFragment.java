package com.bekoal.xpense;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ListFragment;
import android.widget.Toast;

import com.bekoal.xpense.service.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

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
        initAdapter();

        getListView().setDividerHeight(20);

        return;
    }

    private void initAdapter()
    {
        expenseAdapter = new ExpenseAdapter(getActivity().getApplicationContext());
        setListAdapter(expenseAdapter);

        Bundle bundleArgs = getArguments();

        String newString = new String();
        newString = bundleArgs.getString(queryString);

        Cursor c = db.rawQuery(newString, null);
        while(c.moveToNext()) {

            String[] args = new String[c.getColumnCount()];
            for (int i = 0; i < c.getColumnCount(); i++) {
                args[i] = c.getString(i);
            }
            Expense newExpense = new Expense(args);
            expenseAdapter.addExpense(newExpense);

        }
    }


    @Override
    public void onActivityCreated(Bundle savedState) {
        super.onActivityCreated(savedState);

        // Allows users to edit or delete trips
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, final View arg1, int arg2, long arg3) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("Modify expense?");
                dialog.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Edit the trip
                        // TODO : Edit
                        dialog.cancel();
                    }
                });

                dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the trip from the list and the db?
                        // Maybe add a confirm alert?
                        final AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(getActivity());
                        confirmDeleteDialog.setTitle("Are you sure you want to delete this item?");
                        confirmDeleteDialog.setMessage("This action is not reversible.");
                        confirmDeleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO : Delete
                                String Message = "Expense deleted";
                                try {
                                    Expense expense = (Expense) arg1.getTag();
                                    int expenseID = expense.getExpenseID();
                                    String query = String.format("DELETE FROM Expenses WHERE ExpenseID = %s;", expenseID);
                                    db.execSQL(query);
                                    initAdapter();
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                    Message = "Error occurred during deletion";
                                }

                                Toast.makeText(getActivity(), Message, Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }
                        });
                        confirmDeleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        confirmDeleteDialog.create().show();
                        dialog.cancel();
                    }
                });

                dialog.create().show();

                return true;
            }
        });
    }



}

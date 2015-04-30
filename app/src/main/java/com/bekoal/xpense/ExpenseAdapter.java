package com.bekoal.xpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tim on 4/16/2015.
 */
public class ExpenseAdapter extends BaseAdapter {

    private final List<Expense>mExpenses = new ArrayList<Expense>();
    private LayoutInflater mInflater;
    private final Context mContext;

    public ExpenseAdapter(Context context){
        mContext = context;
    }

    public void addExpense(Expense expense){
        mExpenses.add(expense);
        notifyDataSetChanged();
    }

    public long getItemId(int pos) {
        return pos;
    }

    public Object getItem(int pos) {
        return mExpenses.get(pos);
    }

    public int getCount() { return mExpenses.size(); }



    public View getView(int pos, View resultingView, ViewGroup parent){
        final Expense expense = (Expense) getItem(pos);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resultingView = mInflater.inflate(R.layout.activity_expense, null);


        final TextView typeView = (TextView) resultingView.findViewById(R.id.type);
        String typeText = expense.getType().toString();
        typeView.setText(typeText);

        final TextView descriptionView = (TextView) resultingView.findViewById(R.id.description);
        String descriptionText = expense.getDescription();
        descriptionView.setText(descriptionText);

        final TextView costView = (TextView) resultingView.findViewById(R.id.cost);
        String costText = Double.toString(expense.getAmount());
        costView.setText(costText);

        final TextView dateView = (TextView) resultingView.findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        if(expense.getDate() != null) {
            String dateText = dateFormat.format(expense.getDate());
            dateView.setText(dateText);
        }
//        String dateText = expense.getDate().toString();

        resultingView.setTag(getItem(pos));


        return resultingView;
    }

}

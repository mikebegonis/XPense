package com.bekoal.xpense;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.bekoal.xpense.service.TravelModeCommands;
import com.bekoal.xpense.service.TravelModeService;

public class AddFragment extends Fragment {

    ImageButton btnConfirm = null;
    ImageButton btnCancel = null;
    EditText txtDateTimeExpense = null;
    EditText txtAmountExpense = null;
    Spinner spinnerExpenseType = null;
    EditText txtDescription = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_fragment, container, false);

        btnConfirm = (ImageButton)v.findViewById(R.id.btn_add_expense_confirm);
        btnCancel = (ImageButton)v.findViewById(R.id.btn_add_expense_cancel);
        txtDateTimeExpense = (EditText)v.findViewById(R.id.txtDateTime_Expense);
        txtAmountExpense = (EditText)v.findViewById(R.id.txtAmount_Expense);
        txtDescription = (EditText)v.findViewById(R.id.txtDescription_Expense);
        spinnerExpenseType = (Spinner)v.findViewById(R.id.spinnerExpenseType);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQuery = "INSERT INTO Expenses VALUES(";
                strQuery += String.format("'%s', %s, '%s', NULL, NULL, 0, 0, 0, '%s');",
                        txtDateTimeExpense.getText().toString(), txtAmountExpense.getText().toString(),
                        txtDescription.getText().toString(),
                        spinnerExpenseType.getSelectedItem().toString());

                ((MainActivity)getActivity()).getDatabase().execSQL(strQuery);


//                Intent intent = new Intent(getActivity(), TravelModeService.class);
//                intent.putExtra(TravelModeCommands.EXECUTE_INSERT, strQuery);
//                getActivity().startService(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

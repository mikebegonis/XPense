package com.bekoal.xpense;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bekoal.xpense.service.DatabaseHelper;

public class AddTripFragment extends Fragment {
    String mCurrentPhotoPath = null;
    ImageButton btnConfirm = null;
    ImageButton btnCancel = null;

    EditText txtDestination = null;
    EditText txtStartDate = null;
    EditText txtEndDate = null;
    EditText txtNotes = null;

    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_trip_fragment, container, false);

        btnConfirm = (ImageButton) v.findViewById(R.id.btn_add_expense_confirm);
        btnCancel = (ImageButton) v.findViewById(R.id.btn_add_expense_cancel);
        txtDestination = (EditText) v.findViewById(R.id.destination);
        txtStartDate = (EditText) v.findViewById(R.id.startDate);
        txtEndDate = (EditText) v.findViewById(R.id.endDate);
        txtNotes = (EditText) v.findViewById(R.id.note);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strQuery = "INSERT INTO Travel("
                        + "StartDate, EndDate, Title, Status, Note) VALUES(";
                strQuery += String.format("'%s', '%s', '%s', '%s', '%s');",
                        txtStartDate.getText().toString(),
                        txtEndDate.getText().toString(),
                        txtDestination.getText().toString().replace("'",""),
                        "NOT_DONE",
                        txtNotes.getText().toString().replace("'","//"));

                ((MainActivity) getActivity()).getDatabase().execSQL(strQuery);
                Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDestination.setText("");
                txtNotes.setText("");
                txtStartDate.setText("");
                txtEndDate.setText("");
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
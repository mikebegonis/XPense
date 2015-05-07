package com.bekoal.xpense;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bekoal.xpense.service.DatabaseHelper;

import java.util.Calendar;

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

    @Override
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

        txtStartDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;

                if (action == MotionEvent.ACTION_DOWN) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtStartDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
                    dpd.show();
                }

                return true;
            }
        });

        txtEndDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction() & MotionEvent.ACTION_MASK;

                if (action == MotionEvent.ACTION_DOWN) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            txtEndDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);

                    dpd.show();
                }

                return true;
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
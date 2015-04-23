package com.bekoal.xpense;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bekoal.xpense.service.DatabaseHelper;
import com.bekoal.xpense.service.TravelModeCommands;
import com.bekoal.xpense.service.TravelModeService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFragment extends Fragment {

    String mCurrentPhotoPath = null;
    ImageButton btnConfirm = null;
    ImageButton btnCancel = null;
    EditText txtDateTimeExpense = null;
    EditText txtAmountExpense = null;
    Spinner spinnerExpenseType = null;
    EditText txtDescription = null;
    Button addReceiptButton = null;
    ImageView receiptImage = null;

    CheckBox expenseCheckBox = null;
    CheckBox tripCheckBox = null;
    EditText txtDestination = null;
    EditText txtStartDate = null;
    EditText txtEndDate = null;
    EditText txtNotes = null;
    Spinner spinnerTrip = null;

    ArrayAdapter<String> spinnerAdapter = null;

    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_fragment, container, false);

        btnConfirm = (ImageButton)v.findViewById(R.id.btn_add_expense_confirm);
        btnCancel = (ImageButton)v.findViewById(R.id.btn_add_expense_cancel);
        txtDateTimeExpense = (EditText)v.findViewById(R.id.txtDateTime_Expense);
        txtAmountExpense = (EditText)v.findViewById(R.id.txtAmount_Expense);
        txtDescription = (EditText)v.findViewById(R.id.txtDescription_Expense);
        spinnerExpenseType = (Spinner)v.findViewById(R.id.spinnerExpenseType);
        addReceiptButton = (Button) v.findViewById(R.id.add_receipt_button);
        receiptImage = (ImageView) v.findViewById(R.id.receipt_image);


        expenseCheckBox = (CheckBox)v.findViewById(R.id.expenseCheckBox);
        tripCheckBox = (CheckBox)v.findViewById(R.id.tripCheckBox);
        txtDestination = (EditText)v.findViewById(R.id.destination);
        txtStartDate = (EditText)v.findViewById(R.id.startDate);
        txtEndDate = (EditText)v.findViewById(R.id.endDate);
        txtNotes = (EditText)v.findViewById(R.id.note);
        spinnerTrip = (Spinner)v.findViewById(R.id.spinnerTrip);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();
        spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrip.setAdapter(spinnerAdapter);

        Cursor c = db.rawQuery("SELECT TravelID FROM Travel", null);
        while(c.moveToNext()) {
            if (c.moveToNext()) {
                String arg = new String();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    arg = c.getString(i);
                }
                spinnerAdapter.add(arg);
            }
        }

        expenseCheckBox.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(tripCheckBox.isChecked())
                    tripCheckBox.setChecked(false);
            }
        });

        tripCheckBox.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(expenseCheckBox.isChecked())
                    expenseCheckBox.setChecked(false);
            }
        });

        addReceiptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
//                intent.setType("image/*");
//                intent.putExtra("return-data", true);

                File photoFile = null;

                // Create an image file name
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    photoFile = File.createTempFile(
                            imageFileName,  /* prefix */
                            ".jpg",         /* suffix */
                            storageDir      /* directory */
                    );

                    // Save a file: path for use with ACTION_VIEW intents
                    mCurrentPhotoPath =  photoFile.getAbsolutePath();
                } catch (IOException e) {
                    // Error
                    Log.e("Camera", "Unable to create file for photo");
                }

                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, MainActivity.TAKE_PICTURE);
                }
            }
        });

        receiptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(getImageUri(getActivity(), ((BitmapDrawable) receiptImage.getDrawable()).getBitmap()));
                startActivity(intent);
            }
        });



        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(expenseCheckBox.isChecked() && !tripCheckBox.isChecked()) {
                    String strQuery = "INSERT INTO Expenses("
                            + "Date, Amount, Description, Img, Location, Type, TravelID) VALUES(";
                    try {
                        strQuery += String.format("'%s', %s, '%s', '%s', NULL, '%s', '%s');",
                                txtDateTimeExpense.getText().toString(),
                                txtAmountExpense.getText().toString(),
                                txtDescription.getText().toString().replace("'", "''"),
                                mCurrentPhotoPath,
                                spinnerExpenseType.getSelectedItem().toString(),
                                spinnerTrip.getSelectedItem().toString());

                        ((MainActivity) getActivity()).getDatabase().execSQL(strQuery);
                    } catch (Exception e) {
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "Please add a trip before adding an expense.", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }


                else if(!expenseCheckBox.isChecked() && tripCheckBox.isChecked()) {
                    String strQuery = "INSERT INTO Travel("
                            + "StartDate, EndDate, Title, Status, Note, TravelID) VALUES(";
                    strQuery += String.format("'%s', '%s', '%s', '%s', '%s', NULL);",
                            txtStartDate.getText().toString(),
                            txtEndDate.getText().toString(),
                            txtDestination.getText().toString(),
                            "NOT_DONE",
                            txtNotes.getText());

                    ((MainActivity) getActivity()).getDatabase().execSQL(strQuery);
                }


                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Make sure a checkbox is selected!",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAmountExpense.setText("");
                txtDateTimeExpense.setText("");
                txtDescription.setText("");
                spinnerExpenseType.setSelection(0);
                spinnerTrip.setSelection(0);
                receiptImage.setVisibility(View.INVISIBLE);
                receiptImage.setImageBitmap(null);


                expenseCheckBox.setChecked(false);
                tripCheckBox.setChecked(false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MainActivity.TAKE_PICTURE) {
            // Add the photo to a gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

            // Create a preview bitmap
            receiptImage.setVisibility(View.VISIBLE);
            int targetW = R.dimen.receipt_preview_width; // receiptImage.getWidth(); // = 0 for some reason even though its now visible
            int targetH = R.dimen.receipt_preview_height; // receiptImage.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
//            bmOptions.inPurgeable = true;

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                receiptImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Failed to load receipt image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
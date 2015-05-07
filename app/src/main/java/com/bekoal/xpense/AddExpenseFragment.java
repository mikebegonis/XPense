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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.bekoal.xpense.service.DatabaseHelper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseFragment extends Fragment {

    String mCurrentPhotoPath = null;
    ImageButton btnConfirm = null;
    ImageButton btnCancel = null;
    EditText txtDateTimeExpense = null;
    EditText txtAmountExpense = null;
    Spinner spinnerExpenseType = null;
    EditText txtDescription = null;
    Button addReceiptButton = null;
    ImageView receiptImage = null;
    Spinner spinnerTrip = null;

    TripSpinnerAdapter spinnerAdapter = null;

    private DatabaseHelper dbHelper = null;
    private SQLiteDatabase db = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_expense_fragment, container, false);

        btnConfirm = (ImageButton)v.findViewById(R.id.btn_add_expense_confirm);
        btnCancel = (ImageButton)v.findViewById(R.id.btn_add_expense_cancel);
        txtDateTimeExpense = (EditText)v.findViewById(R.id.txtDateTime_Expense);
        txtAmountExpense = (EditText)v.findViewById(R.id.txtAmount_Expense);
        txtDescription = (EditText)v.findViewById(R.id.txtDescription_Expense);
        spinnerExpenseType = (Spinner)v.findViewById(R.id.spinnerExpenseType);
        addReceiptButton = (Button) v.findViewById(R.id.add_receipt_button);
        receiptImage = (ImageView) v.findViewById(R.id.receipt_image);
        spinnerTrip = (Spinner)v.findViewById(R.id.spinnerTrip);

        dbHelper = new DatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getReadableDatabase();
        spinnerAdapter = new TripSpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrip.setAdapter(spinnerAdapter);

        Cursor c = db.rawQuery("SELECT * FROM Travel", null);
        while(c.moveToNext()) {

            String[] arg = new String[c.getColumnCount()];
            for (int i = 0; i < c.getColumnCount(); i++) {
                arg[i] = c.getString(i);
            }
            spinnerAdapter.add(new Trip(arg));

        }

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

                BigDecimal amount = new BigDecimal(txtAmountExpense.getText().toString());

                String travelID = ((Trip)spinnerTrip.getSelectedView().getTag()).getmTravelID();
//                if(spinnerTrip.getSelectedItemPosition() == 0)
//                    travelID = "2";
//                else
//                    travelID = Integer.toString((spinnerTrip.getSelectedItemPosition() + 1) * 2);




                String strQuery = "INSERT INTO Expenses("
                        + "Date, Amount, Description, Img, Location, Type, TravelID) VALUES(";
                try {
                    Trip trip = (Trip)spinnerExpenseType.getSelectedView().getTag();
                    strQuery += String.format("'%s', %s, '%s', '%s', NULL, '%s', %s);",
                            txtDateTimeExpense.getText().toString(),
                            amount,
                            txtDescription.getText().toString().replace("'", "''"),
                            mCurrentPhotoPath,
                            spinnerExpenseType.getSelectedItem().toString().replace(" ", ""),
                            travelID);

                    ((MainActivity) getActivity()).getDatabase().execSQL(strQuery);
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Please fill in more information", Toast.LENGTH_LONG);
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
                receiptImage.setVisibility(View.GONE);
                receiptImage.setImageBitmap(null);
            }
        });

        Bundle args = getArguments();
        if(args != null
                && args.containsKey("NAME"))
        {
            txtDescription.setText(args.getString("NAME", ""));

        }

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
package com.bekoal.xpense;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
                String strQuery = "INSERT INTO Expenses("
                        + "Date, Amount, Description, Img, Location, Type) VALUES(";
                strQuery += String.format("'%s', %s, '%s', '%s', NULL, '%s');",
                        txtDateTimeExpense.getText().toString(), txtAmountExpense.getText().toString(),
                        txtDescription.getText().toString().replace("'", "''"),
                        mCurrentPhotoPath,
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
//            float targetW = (float)R.dimen.receipt_preview_width; // receiptImage.getWidth(); // = 0 for some reason even though its now visible
//            float targetH = (float)R.dimen.receipt_preview_height; // receiptImage.getHeight();
            float targetW = 1920;
            float targetH = 1080;

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            float photoW = (float)bmOptions.outWidth;
            float photoH = (float)bmOptions.outHeight;
//            int photoW = 1920;
//            int photoH = 1080;

            // Determine how much to scale down the image
            int scaleFactor = Math.max(1, Math.round(Math.min(photoW / targetW, photoH / targetH)));

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

//            bmOptions.inPurgeable = true;

            try {
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap scaledBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                Bitmap bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

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

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}

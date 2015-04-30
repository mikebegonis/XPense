package com.bekoal.xpense;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class AddFragment extends Fragment {

    private AddTripFragment mAddTripFragment;
    private AddExpenseFragment mAddExpenseFragment;

    String mCurrentPhotoPath = null;
    ImageView receiptImage = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.add_fragment, container, false);

        mAddTripFragment = new AddTripFragment();
        mAddExpenseFragment = new AddExpenseFragment();



        // Create buttons
        final Button addTripButton = (Button) v.findViewById(R.id.add_trip_button);
        final Button addExpenseButton = (Button) v.findViewById(R.id.add_expense_button);

        addTripButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.add_fragment_container, mAddTripFragment).commit();
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.add_fragment_container, mAddExpenseFragment).commit();
            }
        });


        // Default to the add trip fragment
        FragmentManager mFragmentManager = getFragmentManager();

        FragmentTransaction fTransaction = mFragmentManager.beginTransaction();

        Fragment fragment = mAddTripFragment;

        try
        {
            Bundle args = getArguments();
            if(args.containsKey("NAME"))
            {
                mAddExpenseFragment.setArguments(args);
                fragment = mAddExpenseFragment;
                ((RadioButton)addExpenseButton).toggle();

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        fTransaction.add(R.id.add_fragment_container, fragment);
        fTransaction.commit();



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
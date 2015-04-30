package com.bekoal.xpense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by begon_000 on 4/30/2015.
 */
public class AddExpenseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.putExtra("NAME", intent.getStringExtra("NAME"));
        mainIntent.putExtra("ADDRESS", intent.getStringExtra("ADDRESS"));
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(mainIntent);



    }
}

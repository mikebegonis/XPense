package com.bekoal.xpense;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bekoal.xpense.service.TravelModeCommands;
import com.bekoal.xpense.service.TravelModeService;

public class TravelModeActivity extends ActionBarActivity {

    private ServiceConnection _serviceConn = null;

    private TravelModeService _service = null;

    private BroadcastReceiver _receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        // Create buttons
        final Button summaryButton = (Button) findViewById(R.id.summary_button);
        final Button addButton = (Button) findViewById(R.id.add_button);
        final Button travelModeButton = (Button) findViewById(R.id.travel_mode_button);

        summaryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TravelModeActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TravelModeActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        travelModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Dummy button, does nothing
            }
        });



        _receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = intent.getStringExtra(TravelModeCommands.TEST_CONNECTION);
                Toast.makeText(TravelModeActivity.this.getApplicationContext(), data, Toast.LENGTH_LONG).show();
            }
        };

        BroadcastReceiver _queryReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = intent.getStringExtra(TravelModeCommands.EXECUTE_QUERY);
                Toast.makeText(TravelModeActivity.this.getApplicationContext(), data, Toast.LENGTH_LONG).show();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(_receiver,
            new IntentFilter(TravelModeCommands.TEST_CONNECTION));

        LocalBroadcastManager.getInstance(this).registerReceiver(_queryReciever,
                new IntentFilter(TravelModeCommands.EXECUTE_QUERY));

//        _serviceConn = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                _service = ((TravelModeService.TravelModeBinder)service).getService();
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                _service = null;
//            }
//        };
//
//        bindService(new Intent(this, TravelModeService.class), _serviceConn, BIND_AUTO_CREATE);


    }

    // When a user interacts with the checkbox:
    public void onCheckBoxClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.summary_checkbox) {
            if (isChecked) {
//                Toast travelToastCheck = Toast.makeText(getApplicationContext(),
//                        "This did absolutely nothing.... for now....",
//                        Toast.LENGTH_LONG);
//                travelToastCheck.show();
                StartTravelMode();
            } else {
//                Toast travelToastUncheck = Toast.makeText(getApplicationContext(),
//                        "You just undid absolutely nothing... for now ...",
//                        Toast.LENGTH_LONG);
//                travelToastUncheck.show();
            }
        }

    }

    private void StartTravelMode()
    {
        Intent intent = new Intent(this, TravelModeService.class);
        intent.putExtra(TravelModeCommands.EXECUTE_QUERY, "SELECT * FROM Expenses");
        startService(intent);

//        Intent intent = new Intent(this, TravelModeService.class);
//        intent.putExtra(TravelModeCommands.TEST_CONNECTION, "Message Sent to Service aaaaand ");
//
////        _service.sendBroadcast(intent);
//        startService(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

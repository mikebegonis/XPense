package com.bekoal.xpense;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;

import com.bekoal.xpense.service.TravelModeService;

public class MainActivity extends ActionBarActivity {

    private ServiceConnection _serviceConn = null;

    private TravelModeService _service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create buttons
        final Button summaryButton = (Button) findViewById(R.id.summary_button);
        final Button addButton = (Button) findViewById(R.id.add_button);
        final Button travelModeButton = (Button) findViewById(R.id.travel_mode_button);

        summaryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        travelModeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TravelModeActivity.class);
                startActivity(intent);
            }
        });

        startService(new Intent(this, TravelModeService.class));

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

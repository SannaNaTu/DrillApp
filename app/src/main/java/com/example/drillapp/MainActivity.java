package com.example.drillapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.drillapp.AirplanemodeReceiver.AirplanemodeReceiver;
import com.example.drillapp.Clock.AlarmClockActivity;


import com.example.drillapp.Game.GameActivity;
import com.example.drillapp.Jokes.JokeActivity;
import com.example.drillapp.Test.TestActivity;
import com.example.drillapp.Timer.TimerActivity;
import com.example.drillapp.Url.UrlActivity;
import com.example.drillapp.ContactInformation.ContactInformationActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyAppMessage";

    TextView view, buttonTextView, locationView;

    ImageButton starButton;
    AirplanemodeReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addListenerOnButton();

        br = new AirplanemodeReceiver();

        locationView = findViewById(R.id.locationView);
        LocationManager locationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//            makeUseOfNewLocation(location);
                Log.i(TAG, "onCreate:" + location.getLongitude());
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        }


    }
    public void addListenerOnButton() {

        starButton = findViewById(R.id.starButton);
        buttonTextView = findViewById(R.id.buttonTextView);


        // häviävä tekstibutton
        starButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "activating play view");
                if (buttonTextView.getVisibility() == View.VISIBLE) {
                    buttonTextView.setVisibility(View.INVISIBLE);
                } else {
                    buttonTextView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (item.getItemId()) {
            case R.id.menuitemurl:
                startActivity(new Intent(this, UrlActivity.class));
                return (true);

            case R.id.menuitemjoke:
                startActivity(new Intent(this, JokeActivity.class));
                return (true);

            case R.id.menuitemgame:
                startActivity(new Intent(this, GameActivity.class));

                return (true);

            case R.id.menuitemtest:
                startActivity(new Intent(this, TestActivity.class));
                return (true);

            case R.id.menuitemclock:
                startActivity(new Intent(this, AlarmClockActivity.class));
                return (true);
            case R.id.menuitemtimer:
                startActivity(new Intent(this, TimerActivity.class));
                return (true);
            case R.id.menuitemcontact:
                startActivity(new Intent(this, ContactInformationActivity.class));
                return (true);

        }

        return super.onOptionsItemSelected(item);
    }
    public void google(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.google.fi"));
        startActivity(intent);
    }

    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(br, filter);


    }

    public void onPause() {
        super.onPause();
        this.unregisterReceiver(br);

//        tämä ei vörki


    }

    private void updateLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, we get just a single address.
                    1);
            locationView.setText(String.valueOf(addresses.get(0).getAddressLine(0)));
        } catch (IOException ioException) {
            // Catch network or other I/O problems.

            Log.e(TAG, "OnLocationChanged", ioException);
        }
    }

}

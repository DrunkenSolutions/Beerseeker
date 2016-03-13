package com.dmytryk.beerseeker;


import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    TextView tvEnabled;
    TextView tvStatus;
    TextView tvLocation;
    TextView tvTitle;

    GoogleApiClient googleApiClient;

    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        //Google API
       /* if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API);
        }*/

        tvTitle = (TextView) findViewById(R.id.tvTitleGPS);
        tvStatus = (TextView) findViewById(R.id.tvStatusGPS);
        tvEnabled = (TextView) findViewById(R.id.tvEnabledGPS);
        tvLocation = (TextView) findViewById(R.id.tvLocationGPS);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10,
                    10, locationListener);
            checkEnabled();
        }catch (SecurityException e){
            Toast.makeText(this, "Please, enable GPS at your device", Toast.LENGTH_LONG );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(locationListener);
        }catch (SecurityException e){
            Toast.makeText(this, "Please, enable GPS at your device", Toast.LENGTH_LONG );
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatus.setText("Status: " + String.valueOf(status));
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            try {
                showLocation(locationManager.getLastKnownLocation(provider));
            } catch (SecurityException e){
                Log.d("GPS", "FUCKING ERROR");
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }
    };

    private void showLocation(Location location){
        if (location == null){
            return;
        }
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocation.setText(formatLocation(location));
        }
    }

    private String formatLocation(Location location){
        if (location == null){
            return " ";
        }
        return String.format("Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(location.getTime()));
    }

    private void checkEnabled(){
        Log.d("GPS", "CHECK ENABLED");
        tvEnabled.setText(
                "Enabled: " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    private void onClickLocationSettings(View view){
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection failed", Toast.LENGTH_LONG).show();
    }
}

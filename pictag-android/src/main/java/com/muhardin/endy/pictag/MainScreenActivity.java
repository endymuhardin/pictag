package com.muhardin.endy.pictag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by endy on 12/10/13.
 */
public class MainScreenActivity extends Activity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        Intent fromPrevPage = getIntent();
        String username = fromPrevPage.getStringExtra("username");

        EditText txtUsernameOutput = (EditText) findViewById(R.id.txtUsernameOutput);
        txtUsernameOutput.setText(username, TextView.BufferType.EDITABLE);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new PictagLocationListener();
    }

    @Override
    protected void onResume() {
        String provider = LocationManager.GPS_PROVIDER;
        Integer updateInterval = 3 * 1000; // 3 seconds
        Float distanceDifference = 3F; // 3 meters
        locationManager.requestLocationUpdates(provider,
                updateInterval, distanceDifference, locationListener);
    }

    @Override
    protected void onPause() {
        locationManager.removeUpdates(locationListener);
    }

    private class PictagLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String currentLocation = "Lat : "+location.getLatitude();
            currentLocation += ",Lon : "+location.getLongitude();
            currentLocation += ",Alt : "+location.getAltitude();

            Log.d(MainScreenActivity.class.getName(), "Lat : " + location.getLatitude());
            Log.d(MainScreenActivity.class.getName(), "Lon : " + location.getLongitude());
            Log.d(MainScreenActivity.class.getName(), "Alt : " + location.getAltitude());

            TextView txtLocation = (TextView) findViewById(R.id.txtLocation);
            txtLocation.setText(currentLocation);

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
    }
}
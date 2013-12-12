package com.muhardin.endy.pictag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

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
        super.onResume();
        String provider = LocationManager.GPS_PROVIDER;
        Integer updateInterval = 3 * 1000; // 3 seconds
        Float distanceDifference = 3F; // 3 meters
        locationManager.requestLocationUpdates(provider,
                updateInterval, distanceDifference, locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
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

            if(!Geocoder.isPresent()){
                EditText txtAddress = (EditText) findViewById(R.id.txtAddress);
                txtAddress.setText("Address lookup service not available");
                return;
            }

            new LookupAddress().execute(location);
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

    private class LookupAddress extends AsyncTask<Location, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainScreenActivity.this, "Fetching address", Toast.LENGTH_SHORT);
        }

        @Override
        protected String doInBackground(Location... coordinates) {
            try {
                Geocoder g = new Geocoder(MainScreenActivity.this);
                Double lat = coordinates[0].getLatitude();
                Double lon = coordinates[0].getLongitude();
                List<Address> result = g.getFromLocation(lat, lon, 1);
                if (result.isEmpty()) {
                    return "No address found";
                }

                Address a = result.get(0);
                return a.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            EditText txtAddress = (EditText) findViewById(R.id.txtAddress);
            txtAddress.setText(address);
        }
    }
}
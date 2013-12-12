package com.muhardin.endy.pictag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

    private static final Integer CAMERA_REQUEST_CODE = 17; // some arbitrary number
    private Bitmap capturedImage;

    // initiate the capture
    public void capture(View btnCapture) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
    }

    // receive capture result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Activity.RESULT_OK != resultCode) {
            Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT);
            return;
        }

        try {
            if(CAMERA_REQUEST_CODE != requestCode){
                Log.d(MainScreenActivity.class.getName(), "Request Code : " + requestCode);
                Toast.makeText(this, "Request Code : "+requestCode, Toast.LENGTH_SHORT);
                return;
            }

            if(Activity.RESULT_OK != resultCode){
                Log.d(MainScreenActivity.class.getName(), "Result Code : " + resultCode);
                Toast.makeText(this, "Result Code : "+resultCode, Toast.LENGTH_SHORT);
                return;
            }

            // recycle old image
            if(capturedImage != null){
                capturedImage.recycle();
            }
            InputStream imageStream = getContentResolver().openInputStream(data.getData());
            capturedImage = BitmapFactory.decodeStream(imageStream);

            ImageView display = (ImageView) findViewById(R.id.imgCaptured);
            display.setImageBitmap(capturedImage);
            imageStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void getLastLocation(View btnLastLocation) {
        Location last = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        new LookupAddress().execute(last);
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
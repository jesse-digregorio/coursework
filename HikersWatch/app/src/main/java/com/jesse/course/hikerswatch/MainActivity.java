package com.jesse.course.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latTextView;
    TextView longTextView;
    TextView accuracyTextView;
    TextView altitiudeTextView;
    TextView addressTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();
        }
    }

    public void startListening() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    public void updateLocationInfo(Location location) {
        Log.i("LocationInfo", location.toString());

        String result;
        result = getResources().getText(R.string.label_lat) + " " + Double.toString(location.getLatitude());
        latTextView.setText(result);

        result = getResources().getText(R.string.label_long) + " " + Double.toString(location.getLongitude());
        longTextView.setText(result);

        result = getResources().getText(R.string.label_accuracy) + " " + Float.toString(location.getAccuracy());
        accuracyTextView.setText(result);

        result = getResources().getText(R.string.label_altitude) + " " + Double.toString(location.getAltitude());
        altitiudeTextView.setText(result);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        if (geocoder == null) { Log.i("DAMMIT!", "why is the geocoder null?!"); }
        try {
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size() > 0) {
                Log.i("Place Info", listAddresses.get(0).toString());
            }
        } catch (IOException e) {
            Log.i("DAMMIT!", "IO Exception");
            Log.i("DAMMIT!", e.getMessage());
            e.printStackTrace();
            addressTextView.setText("");
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTextView = (TextView) findViewById(R.id.latTextView);
        longTextView = (TextView) findViewById(R.id.longTextView);
        accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
        altitiudeTextView = (TextView) findViewById(R.id.altitudeTextView);
        addressTextView = (TextView) findViewById(R.id.addressTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {
            // permissions not required in these versions.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // we DO NOT have permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                // we have permission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
                else {
                    Log.i("LocationInfo", "locationManager returned null for getLastKnownLocation");
                }
            }
        }
    }
}

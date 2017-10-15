package com.example.h.hickersapp;

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


    public  void updateLocationInfo(Location location){
        Log.i("Location info-> ",location.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            startListening();

        }
    }

    public void startListening(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location info-> ",location.toString());

                TextView latText = (TextView) findViewById(R.id.latTextView);
                TextView longText = (TextView) findViewById(R.id.longTextView);
                TextView accText = (TextView) findViewById(R.id.accTextView);
                TextView altText = (TextView) findViewById(R.id.altTextView);


                latText.setText("Latitude: " + location.getLatitude());
                longText.setText("Longitude: " + location.getLongitude());
                accText.setText("Accuracy: " + location.getAccuracy());
                altText.setText("Altitude: " + location.getAltitude());

                Geocoder geocoder = new Geocoder(getApplicationContext() , Locale.getDefault());

                try {

                    String address = "Could not find address";
                    List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    if(listAddress != null && listAddress.size() > 0){
                        Log.i("Place info", listAddress.get(0).toString());

                        address = "";

                        if(listAddress.get(0).getSubThoroughfare() != null){
                            address += listAddress.get(0).getSubThoroughfare()+" ";
                        }
                        if(listAddress.get(0).getThoroughfare() != null){
                            address += listAddress.get(0).getThoroughfare()+"\n";
                        }
                        if(listAddress.get(0).getLocality() != null){
                            address += listAddress.get(0).getLocality()+"\n";
                        }
                        if(listAddress.get(0).getCountryName() != null){
                            address += listAddress.get(0).getCountryName()+" ";
                        }



                    }

                    TextView addressText = (TextView) findViewById(R.id.addressTextView);
                    addressText.setText(address);


                } catch (IOException e) {

                    e.printStackTrace();

                }


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

        if(Build.VERSION.SDK_INT < 23){

            startListening();
        }else{


            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }else{
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER , 0 , 0 , locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location != null){
                    updateLocationInfo(location);
                }
            }

        }


    }
}

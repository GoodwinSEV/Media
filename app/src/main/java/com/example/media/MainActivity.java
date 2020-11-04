package com.example.media;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocation;
    private TextView tvLatitude, tvLongtitude, tvInfo;


    private static final long MINIMUM_DISTANCE_FOR_UPDATES = 1;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
    private Address location;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongtitude = (TextView) findViewById(R.id.textview2);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*
        List<String> providers = mLocationManager.getAllProviders();

        for (int i = 0; i < providers.size(); i++) {
            String providerName = providers.get(i);
            tvInfo.append("\nПровайдер: " + providerName);
            tvInfo.append("\nДоступность: "
                    + mLocationManager.isProviderEnabled(providerName) + "\n");
        }*/

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        String provider = mLocationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocation = mLocationManager.getLastKnownLocation(provider);
        mLocationListener = new myLocationListener() ;

        showCurrentLocation(mLocation);



        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MINIMUM_DISTANCE_FOR_UPDATES, MINIMUM_TIME_BETWEEN_UPDATES, mLocationListener);}
    @Override
    public void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    public void onClick(View v) {
        showCurrentLocation(mLocation);

    }

    protected void showCurrentLocation(Location mLocation) {
        if (location !=null) {
            tvLatitude.setText(String.valueOf(location.getLatitude()));
            tvLongtitude.setText(String.valueOf(location.getLongitude()));
        }
    }

    private class myLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            String message = "Новое местоположение \n Долгота: " +
                    location.getLongitude() + "\n Широта: " + location.getLatitude();
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG)
                    .show();
            showCurrentLocation(mLocation);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(MainActivity.this, "Статус провайдера изменился",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Toast.makeText(MainActivity.this,
                    "Провайдер заблокирован пользователем. GPS выключен",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Toast.makeText(MainActivity.this,
                    "Провайдер включен пользователем. GPS включён",
                    Toast.LENGTH_LONG).show();
        }

    }
}
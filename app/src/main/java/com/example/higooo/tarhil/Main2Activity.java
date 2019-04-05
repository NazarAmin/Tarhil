package com.example.higooo.tarhil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements LocationListener {
    TextView txt;
    Button btn;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATIONPERMISSIONCODE = 1234;

    private boolean locationpermissionsgranted = false;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getlocationpermission();
        Animation animfadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);
        if (locationpermissionsgranted) {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            assert lm != null;
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.onLocationChanged(null);
        }
        btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(Main2Activity.this,MapsActivity.class);
                //startActivity(i);
            }
        });




    }

    @Override
    public void onLocationChanged(Location location) {
        txt = (TextView) findViewById(R.id.outlet);

        if(location == null) {
            txt.setText("0");
        }
        else {

           txt.setText(String.valueOf(Math.round(3.6 * location.getSpeed())));
        }

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

    private void getlocationpermission() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationpermissionsgranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATIONPERMISSIONCODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATIONPERMISSIONCODE);
        }
        ;
    }

}

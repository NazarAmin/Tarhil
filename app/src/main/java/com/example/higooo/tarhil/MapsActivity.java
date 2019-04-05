
package com.example.higooo.tarhil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.*;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.animation.Animation;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import android.view.animation.AnimationUtils;

import dalvik.annotation.TestTargetClass;

import static java.lang.Math.round;
import static javax.xml.datatype.Duration.*;

import java.util.Calendar;

import java.time.Duration;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATIONPERMISSIONCODE = 1234;
    LatLng latLng;
    private boolean track = false;
    private boolean locationpermissionsgranted = false;
    private FusedLocationProviderClient mFusedLocationClient;
    Button btn;
    public String dd;
    Button start;
    Button end;
    boolean online = false;
    TextView Average;
    TextView Distance;
    TextView Maximum;
    TextView Blank, OnRide, paused;
    TextView Dur;
    TextView Durend;
    Button History;
    ArrayList <Float> arrayList  = new ArrayList <Float>();
    ArrayList <Location> distances = new ArrayList<Location>();
    ArrayList <Float> meters = new ArrayList<>();
    int index = 0;
    Timer timer =new Timer();
    Date currentTime;
    FirebaseDatabase database;
    private DatabaseReference reference;
    static final int GPS_REQUEST = 100;
// ..
Bitmap bitmap;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getlocationpermission();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Data_Work");
        //Animation animfadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        //animfadein.start();

 /**       reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
for Receiving Configuration
                UserLocation user = new UserLocation();
                double lo = dataSnapshot.child("Nazar").getValue(UserLocation.class).getLongi();
                double la = dataSnapshot.child("Nazar").getValue(UserLocation.class).getLatti();

                latLng = new LatLng(la,lo);

                mMap.clear();
                MarkerOptions marker = new MarkerOptions().position(latLng);
 mMap.addMarker(new MarkerOptions().position(latLng).title("Member Location"));


                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.dot));
                mMap.addMarker(marker);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/
        History = (Button) findViewById(R.id.listH);
        Average = (TextView) findViewById(R.id.avg);
        Distance = (TextView) findViewById(R.id.dis);
        Maximum = (TextView) findViewById(R.id.msp);
        Dur = (TextView) findViewById(R.id.dur);
        Durend = (TextView) findViewById(R.id.ende);

        Blank = (TextView) findViewById(R.id.blank);
        OnRide = (TextView) findViewById(R.id.onride);
        paused = (TextView) findViewById(R.id.Paused);

        btn = (Button) findViewById(R.id.full);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MapsActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        this.onLocationChanged(null);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        start = (Button) findViewById(R.id.trip);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                track = true;
                arrayList.clear();
                distances.clear();

                boolean online = true;
                currentTime = Calendar.getInstance().getTime();

                OnRide.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(),"Trip Started",Toast.LENGTH_LONG).show();


            }
        });

        end = (Button) findViewById(R.id.reset);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                online = false;
                track = false;
                OnRide.setVisibility(View.INVISIBLE);
                paused.setVisibility(View.VISIBLE);

                float num = 0;
               // float de = 0;
                float max = 0;
                float tot;
                for(float s: arrayList){
                    num = num + s;

                    if (max < s){
                        max = s;
                    }
                }
                tot = num/arrayList.size();
                int ig = round(tot);


                 float totDis = 0;

              for(int po = 0; po< distances.size()-1; po++){
                  meters.add(distances.get(po).distanceTo(distances.get(po+1)));

              }
                for (Float q: meters){

                    totDis = totDis + q;
                }

                Average.setText(String.valueOf(ig));
                Maximum.setText(String.valueOf(round(max)));
                Distance.setText(String.valueOf(round(totDis)));
                Dur.setText((CharSequence) currentTime.toString());
                Durend.setText((CharSequence) Calendar.getInstance().getTime().toString());



                arrayList.clear();
                distances.clear();

                totDis = 0;

                int id = 1;
                int avg2 = Integer.parseInt(Average.getText().toString());
                int max2 = Integer.parseInt(Maximum.getText().toString());
                int dis2 = Integer.parseInt(Distance.getText().toString());
                String Start = Dur.getText().toString();
                String end = Durend.getText().toString();



                Trip employee = new Trip(id,avg2,max2,dis2,Start,end);

                SQLHelperClass db = new SQLHelperClass(getApplicationContext());

                db.addEmployee(employee);
                Toast.makeText(getApplicationContext(),"Trip added succesfully",Toast.LENGTH_LONG).show();

            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IR = new Intent(MapsActivity.this,All_Trips.class);
                startActivity(IR);
            }
        });
    }





    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {

            if (locationpermissionsgranted) {
                final Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            MoveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 5f);
                            Toast.makeText(getApplicationContext(), "Location Found OK", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Location Not Found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        } catch (SecurityException e) {

        }


    }

    private void MoveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationpermissionsgranted = false;
        getlocationpermission();

        switch (requestCode) {
            case LOCATIONPERMISSIONCODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            locationpermissionsgranted = false;
                            return;
                        }
                        locationpermissionsgranted = true;
                       // database = FirebaseDatabase.getInstance();
                        //reference = database.getReference("Locations");
                        initMap();
                    }
                }
            }
        }
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) getApplicationContext());


    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationpermissionsgranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;

            }
            mMap.setMyLocationEnabled(true);
            mMap.setTrafficEnabled(true);
           mMap.getUiSettings().setCompassEnabled(true);


 }
    }

    @Override
    public void onLocationChanged(Location location) {


       // String id = reference.push().getKey();

     //   UserLocation userLocation = new UserLocation(id,"Nazar", location.getLongitude(), location.getLatitude());
     //   UserLocation userLocation = new UserLocation(id,"Nazar", 10.6, 30.335);

        //double lattiude = location.getLatitude();
        //String rtu = "Nazar";
       // UserLocation userLocation = new UserLocation(15.3, 30.55);
       // reference.child(rtu);
       // reference.setValue("try this");
      //  Toast.makeText(MapsActivity.this,String.valueOf(longit),Toast.LENGTH_SHORT).show();



        TextView txt = (TextView) this.findViewById(R.id.speed);

        if(location == null)
        {
            txt.setText("0");
           // dis.setText("0 m");
        }
        else
        {


            Location locationA = new Location("PointA");
           locationA.setLatitude(location.getLatitude());
           locationA.setLongitude(location.getLongitude());

            distances.add(locationA);

            if (track) {
                latLng = new LatLng(location.getLatitude(),location.getLongitude());

                MarkerOptions marker = new MarkerOptions().position(latLng);
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.dot));
                mMap.addMarker(marker);

                UserLocation userlocation = new UserLocation(location.getLongitude(), location.getLatitude());
                reference.child("Nazar").setValue(userlocation);
            }
            // index = index + 1;
            float cs = location.getSpeed();
            float csd = (float) (cs * 3.6);
            int speed2 = Math.round(csd);
           dd = String.valueOf(speed2);
            txt.setText(dd);

            arrayList.add(csd);

           String id = reference.push().getKey();
            UserLocation userLocation = new UserLocation(id,"Nazar", locationA.getLongitude(), locationA.getLatitude());
            reference.child(id).setValue(userLocation);

            // dis.setText(String.valueOf(Math.round(location.getAltitude())) + " m");
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
}

package com.example.higooo.tarhil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class All_Trips extends AppCompatActivity {
    private GoogleMap mMap;
  //  ImageView imageView;
    private static final String TABLE_NAME = "Trips_TB";

    ArrayList<Map<String, String>> empList = new ArrayList<>();

    public All_Trips() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_trips);
      //  imageView = (ImageView) findViewById(R.id.image);


        ListView listView = (ListView) findViewById(R.id.ListView2);
        String[] from = new String[]{"AvgS", "MaxS", "DisS", "StartS", "EndS"};
        int[] to = new int[]{R.id.TVAvg, R.id.TVMax, R.id.TVDis, R.id.TVStart, R.id.TVEnd};

        SQLHelperClass db = new SQLHelperClass(this);

        ArrayList<Trip> emps = db.getEmployees();

        for (Trip e: emps) {
            Map<String, String> map = new LinkedHashMap<>();

            map.put("AvgS", String.valueOf(e.getAvgS()));
            map.put("MaxS", String.valueOf(e.getMaxS()));
            map.put("DisS",String.valueOf(e.getDisS()));
            map.put("StartS",e.getStartS());
            map.put("EndS",e.getEndS());

            empList.add(map);
        }



/*imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent I = new Intent(All_Trips.this,MapsActivity2.class);
        startActivity(I);
    }
});
**/
        ListAdapter adapter = new SimpleAdapter(this, empList, R.layout.list, from, to);
        listView.setAdapter(adapter);

     //   return View;

    }


}

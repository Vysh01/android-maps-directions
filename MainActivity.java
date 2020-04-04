package com.thecodecity.mapsdirection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.thecodecity.mapsdirection.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, com.thecodecity.mapsdirection.directionhelpers.TaskLoadedCallback {

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1, place2;
    Polyline currentPolyline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetDirection = findViewById(R.id.btnGetDirection);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);



        place1 = new MarkerOptions().position(new LatLng(27.658143, 85.3199503)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(27.667491, 85.3208583)).title("Location2");

        String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");
        new com.thecodecity.mapsdirection.directionhelpers.FetchURL(MainActivity.this).execute(url, "driving");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        //Origin route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //Destination route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //mode
        String mode = "mode=" + directionMode;
        // building the parameter to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        //output format
        String output = "json";
        //building url for seb services
        String url = "https://maps.googleapes.com/maps/api/directions/" + output + "?" + parameters + "&keys=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline!= null)
            currentPolyline.remove();
        currentPolyline=map.addPolyline((PolylineOptions) values[0]);

    }
}

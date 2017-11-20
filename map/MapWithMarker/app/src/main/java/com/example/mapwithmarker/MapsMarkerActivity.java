package com.example.mapwithmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private TextView name;
    private TextView time;
    private TextView description;
    private String nameString;
    private String timeString;
    private String descriptionString;
    private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Add title
        getSupportActionBar().setTitle("Event Details");
        // Add Back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Get intent
        Intent intename = getIntent();
        nameString = (String) intename.getSerializableExtra("NAME");
        timeString = (String) intename.getSerializableExtra("TIME");
        descriptionString = (String) intename.getSerializableExtra("DES");
        location = new LatLng((Double) intename.getSerializableExtra("LAT"),
                (Double) intename.getSerializableExtra("LNG"));
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Get TextView
        name = (TextView) findViewById(R.id.Name);
        time = (TextView) findViewById(R.id.Time);
        description = (TextView) findViewById(R.id.Description);
        // Set TextView uneditable
        name.setKeyListener(null);
        time.setKeyListener(null);
        description.setKeyListener(null);
        // Set TextView size and bold
        name.setTextSize(20);
        time.setTextSize(10);
        TextPaint paint = name.getPaint();
        paint.setFakeBoldText(true);
        //Set content
        name.setText(nameString);
        time.setText(timeString);
        description.setText(descriptionString);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(location)
                .title(descriptionString));
        marker.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // Back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

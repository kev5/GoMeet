package com.example.mapwithmarker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

public class IntentTest extends AppCompatActivity {
    private static final String TAG = IntentTest.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;
    private boolean mAddressRequested;
    private ProgressBar mProgressBar;

    /** Called when the activity is first created. */
    EditText nameEditCtrl;
    EditText descriptionEditCtrl;
    EditText timeEditCtrl;
    EditText geoEditCtrl;
    EditText zipEditCtrl;
    EditText addEditCtrl;
    Button btnCtlr;
    Button locBtnCtlr;
    String name;
    String description;
    String time;
    Double lat;
    Double lng;
    String zipcode;
    String address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nameEditCtrl = (EditText) findViewById(R.id.editText1);
        descriptionEditCtrl = (EditText) findViewById(R.id.editText2);
        timeEditCtrl = (EditText) findViewById(R.id.editText3);
        geoEditCtrl = (EditText) findViewById(R.id.editText4);
        zipEditCtrl = (EditText) findViewById(R.id.editText5);
        addEditCtrl = (EditText) findViewById(R.id.editText6);

        geoEditCtrl.setKeyListener(null);
        zipEditCtrl.setKeyListener(null);
        addEditCtrl.setKeyListener(null);

        btnCtlr = (Button) findViewById(R.id.button1);
        btnCtlr.setOnClickListener(new ButtonClickHandler());
        locBtnCtlr = (Button) findViewById(R.id.button2);
        locBtnCtlr.setOnClickListener(new LocationButtonHandler());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        mAddressRequested = false;
        updateUIWidgets();
    }

    /**
     * Go to detail page.
     */
    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (nameEditCtrl != null && nameEditCtrl.getText().length() != 0) {
                name = nameEditCtrl.getText().toString();
            } else {
                showSnackbar("No event name");
                return;
            }
            if (descriptionEditCtrl != null && descriptionEditCtrl.getText().length() != 0) {
                description = descriptionEditCtrl.getText().toString();
            } else {
                showSnackbar("No event description");
                return;
            }
            if (timeEditCtrl != null && timeEditCtrl.getText().length() != 0) {
                time = timeEditCtrl.getText().toString();
            } else {
                showSnackbar("No event time");
                return;
            }
            Intent intObj = new Intent(IntentTest.this,
                    MapsMarkerActivity.class);
            intObj.putExtra("NAME", name);
            intObj.putExtra("DES", description);
            intObj.putExtra("TIME", time);
            intObj.putExtra("LAT", lat);
            intObj.putExtra("LNG", lng);
            intObj.putExtra("ZIPCODE", zipcode);
            intObj.putExtra("ADDRESS", address);
            startActivity(intObj);
        }
    }

    /**
     * Set location.
     */
    public class LocationButtonHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (!checkPermissions()) {
                requestPermissions();
            } else {
                getLastLocation();
            }
        }
    }

    /**
     * Get location.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            lat = mLastLocation.getLatitude();
                            lng = mLastLocation.getLongitude();
                            geoEditCtrl.setText(String.format(Locale.getDefault(), "%f, %f", lat, lng));

                            mAddressRequested = true;
                            updateUIWidgets();
                            final String geocodeUrl = String.format(Locale.getDefault(),
                                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=true&key=AIzaSyCMl39spGvXhB2VW1tjdyXMJOJLIkxkWv0",
                                    lat, lng);
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    HttpGet httpGet = new HttpGet(geocodeUrl);
                                    HttpClient client = new DefaultHttpClient();
                                    HttpResponse response;
                                    StringBuilder stringBuilder = new StringBuilder();

                                    try
                                    {
                                        response = client.execute(httpGet);
                                        HttpEntity entity = response.getEntity();
                                        InputStream stream = entity.getContent();
                                        int b;
                                        while ((b = stream.read()) != -1) {
                                            stringBuilder.append((char) b);
                                        }
                                    } catch (ClientProtocolException e) {
                                    } catch (IOException e) {
                                    }
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject = new JSONObject(stringBuilder.toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    getLoc(jsonObject);
                                }
                            });

                            thread.start();
                            try {
                                thread.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            displayAddressOutput();
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(IntentTest.this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. \
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private void getLoc(JSONObject jsonObj) {
        address = null;
        zipcode = null;
        try {
            String status = jsonObj.getString("status");
            if(status.equalsIgnoreCase("OK")){
                JSONArray results = jsonObj.getJSONArray("results");
                for(int i = 0; i < results.length(); i++) {
                    JSONObject r = results.getJSONObject(i);
                    JSONArray typesArray = r.getJSONArray("types");
                    String types = typesArray.getString(0);
                    if(types.equalsIgnoreCase("premise")) {
                        address = r.getString("formatted_address").split(", ")[0];
                        Log.i("Address", address);
                    } else if(types.equalsIgnoreCase("postal_code")) {
                        zipcode = r.getJSONArray("address_components").
                                getJSONObject(0).getString("long_name");
                        Log.i("Zipcode", zipcode);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("Error", "Failed to load JSON");
            e.printStackTrace();
        }
    }

    private void displayAddressOutput() {
        if(address == null || zipcode == null) {
            showSnackbar("Invalid location.");
        } else {
            addEditCtrl.setText(address);
            zipEditCtrl.setText(zipcode);
        }
        mAddressRequested = false;
        updateUIWidgets();
    }

    private void updateUIWidgets() {
        if (mAddressRequested) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            locBtnCtlr.setEnabled(false);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
            locBtnCtlr.setEnabled(true);
        }
    }
}
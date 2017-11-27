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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

    /** Called when the activity is first created. */
    EditText nameEditCtrl;
    EditText descriptionEditCtrl;
    EditText timeEditCtrl;
    EditText latEditCtrl;
    EditText lngEditCtrl;
    Button btnCtlr;
    Button locBtnCtlr;
    String name;
    String description;
    String time;
    Double lat;
    Double lng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nameEditCtrl = (EditText) findViewById(R.id.editText1);
        descriptionEditCtrl = (EditText) findViewById(R.id.editText2);
        timeEditCtrl = (EditText) findViewById(R.id.editText3);
        latEditCtrl = (EditText) findViewById(R.id.editText4);
        lngEditCtrl = (EditText) findViewById(R.id.editText5);

        btnCtlr = (Button) findViewById(R.id.button1);
        btnCtlr.setOnClickListener(new ButtonClickHandler());
        locBtnCtlr = (Button) findViewById(R.id.button2);
        locBtnCtlr.setOnClickListener(new LocationButtonHandler());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Go to detail page.
     */
    public class ButtonClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (nameEditCtrl != null && nameEditCtrl.getText().length() != 0) {
                name = nameEditCtrl.getText().toString();
            } else {
                name = "Null";
            }
            if (descriptionEditCtrl != null && descriptionEditCtrl.getText().length() != 0) {
                description = descriptionEditCtrl.getText().toString();
            } else {
                description = "Null";
            }
            if (timeEditCtrl != null && timeEditCtrl.getText().length() != 0) {
                time = timeEditCtrl.getText().toString();
            } else {
                time = "Null";
            }
            if (latEditCtrl != null && latEditCtrl.getText().length() != 0) {
                lat = Double.parseDouble(latEditCtrl.getText().toString());
            } else {
                time = "Null";
            }
            if (lngEditCtrl != null && lngEditCtrl.getText().length() != 0) {
                lng = Double.parseDouble(lngEditCtrl.getText().toString());
            } else {
                time = "Null";
            }
            Intent intObj = new Intent(IntentTest.this,
                    MapsMarkerActivity.class);
            intObj.putExtra("NAME", name);
            intObj.putExtra("DES", description);
            intObj.putExtra("TIME", time);
            intObj.putExtra("LAT", lat);
            intObj.putExtra("LNG", lng);
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

                            latEditCtrl.setText(String.valueOf(mLastLocation.getLatitude()));
                            lngEditCtrl.setText(String.valueOf(mLastLocation.getLongitude()));
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
}
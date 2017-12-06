package com.example.meetgo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private cards card_data[];
    private com.example.meetgo.arrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;
    private String currentUid;
    private DatabaseReference zipcodeDb;
    ListView listView;
    List<cards> rowItems;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;
//    private ArrayList<String> al;
//    private ArrayAdapter<String> arrayAdapter;
//    private int i;
//    private int m;
    private ProgressBar mProgressBar;
    Double lat, lng, lat_final, lng_final;
    String zipcode = "";
    private boolean location_change;
    private EditText mEventName;
    private Button mPostEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zipcodeDb = FirebaseDatabase.getInstance().getReference().child("posts").child("zipcode");
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        mLastLocation = new Location("");
        mResultReceiver = new AddressResultReceiver(new Handler());
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        rowItems = new ArrayList<cards>();
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        if (!checkPermissions()) {
            requestPermissions();
        }
        location_change = false;
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                zipcodeDb.child(zipcode).child(userId).child("connections").child("nope").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this, "NOPE!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                zipcodeDb.child(zipcode).child(userId).child("connections").child("yep").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this, "Like!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
//                al.add("XML ".concat(String.valueOf(i)));
//                arrayAdapter.notifyDataSetChanged();
//                Log.d("LIST", "notified");
//                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
                cards obj = (cards) dataObject;
                String postDes = obj.getPost();
                Double LAT = obj.getLat();
                Double LNG = obj.getLng();
                Intent intent = new Intent(MainActivity.this,MapsMarkerActivity.class);
                intent.putExtra("LAT",LAT);
                intent.putExtra("LNG",LNG);
                intent.putExtra("PostText",postDes);
                startActivity(intent);
                finish();
                return;
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        lat = Double.parseDouble(intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE));
                        lng = Double.parseDouble(intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE));
                        mLastLocation.setLatitude(lat);
                        mLastLocation.setLongitude(lng);
                        startIntentService();
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );
    }
//    private String userSex;
//    private String notUserSex;
//    public void CheckUserSex(){
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");
//        maleDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if(dataSnapshot.getKey().equals(user.getUid())){
//                    userSex = "Male";
//                    notUserSex = "Female";
//                    getNotUserSex();
//                }
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");
//        femaleDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if(dataSnapshot.getKey().equals(user.getUid())){
//                    userSex = "Female";
//                    notUserSex = "Male";
//                    getNotUserSex();
//                }
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }
//    public void getNotUserSex(){
//        DatabaseReference notUserSexDb = FirebaseDatabase.getInstance().getReference().child("Users").child(notUserSex);
//        notUserSexDb.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if(dataSnapshot.exists()){
//                    al.add(String.valueOf(dataSnapshot.child("Post").getValue()));
//                    arrayAdapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//
//    }
    public void getUserCard(){
        DatabaseReference UserCardDb = FirebaseDatabase.getInstance().getReference().child("posts").child("zipcode").child(zipcode);
        UserCardDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    //cards item =  new cards(dataSnapshot.getKey(),String.valueOf(dataSnapshot.child("postText").getValue()), Double.valueOf(String.valueOf(dataSnapshot.child("Lat").getValue())), Double.valueOf(String.valueOf(dataSnapshot.child("Lng").getValue())));
                    //cards item =  new cards(dataSnapshot.getKey(),String.valueOf(dataSnapshot.child("postText").getValue()), -72.3, 45.0);
                    cards item =  new cards(dataSnapshot.getKey(),String.valueOf(dataSnapshot.child("postText").getValue()), (Double)dataSnapshot.child("Lat").getValue(), (Double)dataSnapshot.child("Lng").getValue());
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    public void getOpendata(){
        final DatabaseReference OpenDataDb = FirebaseDatabase.getInstance().getReference().child("opendata");
        OpenDataDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    cards item =  new cards(dataSnapshot.getKey(),String.valueOf(dataSnapshot.child(zipcode).child("description").getValue()),0.0,0.0);
                    rowItems.add(item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void postEvent(View view){
        Intent intent = new Intent(MainActivity.this,PostActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkGooglePlayServices()) {
            checkInternet();
        } else {
            showToast("No google play service");
        }
    }

    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
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

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public boolean checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private void checkInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            showToast("No internet connection");
            return;
        }

        if (checkPermissions()) {
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            startLocationPermissionRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, LocationMonitoringService.class);
                startService(intent);
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                // showToast(getString(R.string.address_found));
                if (!zipcode.equalsIgnoreCase(resultData.getString(Constants.RESULT_ZIPCODE))) {
                    zipcode = resultData.getString(Constants.RESULT_ZIPCODE);
                    location_change = true;
                    updateCard();
                }
                Log.i("Zipcode", zipcode);
            } else {
                showToast("Address not found");
            }
        }
    }

    public void onDestroy() {
        stopService(new Intent(this, LocationMonitoringService.class));
        super.onDestroy();
    }

    private void updateCard() {
        if (location_change) {
            mProgressBar.setVisibility(View.GONE);
            getUserCard();
            getOpendata();
            location_change = false;
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }
}

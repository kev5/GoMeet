package com.example.meetgo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

public class PostActivity extends AppCompatActivity {
    private EditText mEventName;
    private Button mPostEvent, mGetLocation;
    private FirebaseAuth mAuth;
    private PlacesAutocompleteTextView mAddress;
    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    private boolean mAddressRequested;
    private ProgressBar mProgressBar;

    Double lat, lng, lat_final, lng_final;
    String zipcode = null;
    String address = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // Add title
        getSupportActionBar().setTitle("Post Event");
        // Add Back button
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //CheckUserSex();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mGetLocation = (Button) findViewById(R.id.get_loc);
        mGetLocation.setOnClickListener(new LocationButtonHandler());
        mAddressRequested = false;
        updateUIWidgets();
        mLastLocation = new Location("");
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAuth = FirebaseAuth.getInstance();
        mEventName = (EditText) findViewById(R.id.event_name);
        mAddress = (PlacesAutocompleteTextView) findViewById(R.id.venue);
        mAddress.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                mAddress.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Log.d("test", "details " + details);

                        hideKeyboard(PostActivity.this);

                        for (AddressComponent component : details.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        break;
                                    case LOCALITY:
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        break;
                                    case POSTAL_CODE:
                                        zipcode = component.long_name;
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                        address = details.formatted_address;
                        lat_final = details.geometry.location.lat;
                        lng_final = details.geometry.location.lng;
                        Log.i("Lat, lng", String.format("%f, %f", lat_final, lng_final));
                        // displayAddressOutput();
                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        lat = Double.parseDouble(intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE));
                        lng = Double.parseDouble(intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE));
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        mPostEvent = (Button) findViewById(R.id.go_create);
        mPostEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String eventName = mEventName.getText().toString();
                final String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference postDb = FirebaseDatabase.getInstance().getReference().child("posts").child(userId).child("postText");
                postDb.setValue(eventName);
                Intent intent = new Intent(PostActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LocationMonitoringService.class);
        startService(intent);
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public class LocationButtonHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (lat != null && lng != null) {
                Log.i("Lat, lng", String.format("%f, %f", lat, lng));
                lat_final = lat;
                lng_final = lng;
                mLastLocation.setLatitude(lat);
                mLastLocation.setLongitude(lng);
                startIntentService();
                mAddressRequested = true;
                updateUIWidgets();
            } else {
                showToast("No location");
            }
        }
    }

    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
                address = resultData.getString(Constants.RESULT_ADDRESS);
                zipcode = resultData.getString(Constants.RESULT_ZIPCODE);
                Log.i("Address", address);
                Log.i("Zipcode", zipcode);
                displayAddressOutput();
            } else {
                showToast("Address not found");
            }
            mAddressRequested = false;
            updateUIWidgets();
        }
    }

    private void displayAddressOutput() {
        if(address == null) {
            showSnackbar("Invalid location.");
        } else {
            mAddress.setText(address);
        }
        if(zipcode == null) {
            showSnackbar("Invalid location.");
        } else {
            Log.i("Zipcode", zipcode);
        }
        mAddressRequested = false;
        updateUIWidgets();
        stopService(new Intent(this, FetchAddressIntentService.class));
    }

    private void updateUIWidgets() {
        if (mAddressRequested) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            mGetLocation.setEnabled(false);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
            mGetLocation.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                stopService(new Intent(this, LocationMonitoringService.class));
                this.finish(); // Back button
                return true;
        }
        return super.onOptionsItemSelected(item);
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
//
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
//
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
}

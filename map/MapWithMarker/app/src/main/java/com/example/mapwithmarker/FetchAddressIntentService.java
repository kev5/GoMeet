package com.example.mapwithmarker;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    private static final String TAG = FetchAddressIntentService.class.getSimpleName();

    private ResultReceiver mReceiver;
    double lat, lng;
    String address, zipcode;

    public FetchAddressIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        if (mReceiver == null) {
            Log.e(TAG, "No receiver received");
            return;
        }

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        if (location == null) {
            Log.e(TAG, getString(R.string.no_location_data_provided));
            return;
        }
        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.i(TAG, String.format("%f, %f", lat, lng));
        final String geocodeUrl = String.format(Locale.getDefault(),
                "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&result_type=street_address&key=AIzaSyCMl39spGvXhB2VW1tjdyXMJOJLIkxkWv0",
                lat, lng);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGet httpGet = new HttpGet(geocodeUrl);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response;
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    response = client.execute(httpGet);
                    HttpEntity entity = response.getEntity();
                    InputStream stream = entity.getContent();
                    int b;
                    while ((b = stream.read()) != -1) {
                        stringBuilder.append((char) b);
                    }
                } catch (ClientProtocolException e) {
                    Log.e(TAG, "Client protocol execption");
                } catch (IOException e) {
                    Log.e(TAG, "IO execption");
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
        if (address == null && zipcode == null) {
            Log.e(TAG, "No address and zipcode found");
            deliverResultToReceiver(Constants.SUCCESS_RESULT, "No address", "No zipcode");
        } else {
            deliverResultToReceiver(Constants.SUCCESS_RESULT, address, zipcode);
        }
    }

    private void getLoc(JSONObject jsonObj) {
        address = null;
        zipcode = null;
        try {
            String status = jsonObj.getString("status");
            if(status.equalsIgnoreCase("OK")){
                JSONArray results = jsonObj.getJSONArray("results");
                Log.i(TAG, results.toString());
                String[] addressFragments = results.getJSONObject(0).getString("formatted_address").split(", ");
                ArrayList<String> addresses = new ArrayList<>();
                for (int i = 0; i < addressFragments.length - 1; i++) {
                    addresses.add(addressFragments[i]);
                }
                addresses.remove(addresses.size()-1);
                address = TextUtils.join(", ", addresses);
                JSONArray address_components = results.getJSONObject(0).getJSONArray("address_components");
                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject temp = address_components.getJSONObject(i);
                    String types = temp.getJSONArray("types").getString(0);
                    if (types.equalsIgnoreCase("postal_code")) {
                        zipcode = temp.getString("long_name");
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("Error", "Failed to load JSON");
            e.printStackTrace();
        }
    }

    private void deliverResultToReceiver(int resultCode, String address, String zipcode) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_ADDRESS, address);
        bundle.putString(Constants.RESULT_ZIPCODE, zipcode);
        mReceiver.send(resultCode, bundle);
    }
}

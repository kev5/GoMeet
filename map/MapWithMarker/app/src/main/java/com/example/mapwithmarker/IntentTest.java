package com.example.mapwithmarker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IntentTest extends Activity {
    /** Called when the activity is first created. */
    EditText nameEditCtrl;
    EditText descriptionEditCtrl;
    EditText timeEditCtrl;
    EditText latEditCtrl;
    EditText lngEditCtrl;
    Button btnCtlr;
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
    }

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
}
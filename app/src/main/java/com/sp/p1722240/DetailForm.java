package com.sp.p1722240;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DetailForm extends AppCompatActivity {

    private EditText locationName;
    private EditText tripDistance;
    private EditText fuelConsumption;
    private EditText fuelCost;
    private Button buttonSave;
    private Button buttonDel;

    private TripHelper helper = null;
    private String locationID = "";
    private TextView location = null;
    private double latitude = 0.0d;
    private double longitude = 0.0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_form);

        locationName = (EditText) findViewById(R.id.location_name);
        tripDistance = (EditText) findViewById(R.id.trip_distance);
        fuelConsumption= (EditText) findViewById(R.id.fuel_consumption);
        fuelCost = (EditText) findViewById(R.id.fuel_cost);

        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(onSave);
        buttonDel = (Button) findViewById(R.id.button_delete);
        buttonDel.setOnClickListener(onDel);

        helper = new TripHelper(this);

        locationID = getIntent().getStringExtra("ID");
        if (locationID != null){
            load();
        }
    }

    private void load () {
        Cursor c = helper.getById(locationID);
        c.moveToFirst();
        locationName.setText(helper.getLocationName(c));
        latitude = helper.getLatitude(c);
        longitude = helper.getLongitude(c);
        }


    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //To read data from restaurantName EditText
            String nameStr = locationName.getText().toString();

            if (locationID == null)
                helper.insert(nameStr, latitude, longitude);
            else
                helper.update(locationID, nameStr, latitude, longitude);
            finish();
        }
    };

    private View.OnClickListener onDel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(locationID != null) {
                helper.delete(locationID);
                finish();
            }
        }
    };

    public static String getEcon(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("myAppPackage", 0);
        return prefs.getString("fuelEconomy", "economy");
    }

    public static String getType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("myAppPackage", 0);
        return prefs.getString("petType", "type");

    }
}

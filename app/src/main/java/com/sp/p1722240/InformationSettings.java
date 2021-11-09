package com.sp.p1722240;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class InformationSettings extends AppCompatActivity {

    private EditText fuelEconomy;
    private Spinner petType;
    private Button buttonUrl;
    private Button buttonSet;

    private TripHelper helper = null;
    private String locationID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_settings);

        fuelEconomy = (EditText)findViewById(R.id.fuel_economy);
        petType = (Spinner)findViewById(R.id.petrol_type);
        buttonUrl = (Button)findViewById(R.id.button_url);
        buttonSet = (Button)findViewById(R.id.button_set);

        buttonSet.setOnClickListener(onSet);
        buttonUrl.setOnClickListener(onUrl);
        helper = new TripHelper(this);

        String[] items = new String[]{"92-Octane", "95-Octane", "98-Octane"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        petType.setAdapter(adapter);

        fuelEconomy.setText(getFuel(InformationSettings.this));
        //petType.setAdapter(getType(InformationSettings.this));
    }

    private View.OnClickListener onUrl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i;
            i = new Intent(InformationSettings.this, Web.class);
            startActivity(i);

        }
    };

    private View.OnClickListener onSet = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s = fuelEconomy.getText().toString();
            String t = petType.getSelectedItem().toString();
            setInfo(InformationSettings.this, s, t);
        }
    };
    public void setInfo(Context context, String economy, String type) {
        SharedPreferences prefs = context.getSharedPreferences("SHARED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fuelEconomy", economy);
        editor.putString("petType", type);
        editor.commit();
    }

    public String getFuel(Context context){
        SharedPreferences prefs = context.getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        return prefs.getString("fuelEconomy", "");
    }

    public String getType(Context context){
        SharedPreferences prefs = context.getSharedPreferences("SHARED_PREFS",MODE_PRIVATE);
        return prefs.getString("petType","");
    }
}

package com.sp.p1722240;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TabHost;

public class LocationList extends AppCompatActivity {


    private Cursor model = null;
    private LocationAdapter adapter = null;
    private ListView list;
    private TabHost host;
    private TripHelper helper = null;
    private TextView empty;
    private SharedPreferences prefs = null;
    private GPSTracker gpsTracker;
    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private double myLatitude = 0.0d;
    private double myLongitude = 0.0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        empty = (TextView) findViewById(R.id.empty);
        helper = new TripHelper(this);
        list = (ListView) findViewById(R.id.list);

        adapter = new LocationAdapter(this, model, 0);
        list.setOnItemClickListener(onListClick);
        list.setAdapter(adapter);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    gpsTracker = new GPSTracker(LocationList.this);
}

    private void initList() {
        if (model != null)
            model.close();
        model = helper.getAll(prefs.getString("sort_order","locationName"));
        adapter.swapCursor(model);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
            if (key.equals("sort_order"))
                initList();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initList();
        if (model.getCount() > 0)
            empty.setVisibility(View.INVISIBLE);
    }

    protected void  onDestroy() {
        helper.close();
        super.onDestroy();
        gpsTracker.stopUsingGPS();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.map):
                myLatitude = gpsTracker.getLatitude();
                myLongitude = gpsTracker.getLongitude();

                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);
                intent.putExtra("MYLATITUDE", myLatitude);
                intent.putExtra("MYLONGITUDE", myLongitude);
                startActivity(intent);
                break;

            case (R.id.prefs):
                startActivity(new Intent(this, EditPreferences.class));
                break;

            case (R.id.info):
                startActivity(new Intent(this,InformationSettings.class));
                break;
        }
            return super.onOptionsItemSelected(item);
        }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            model.moveToPosition(position);
            String recordID = helper.getID(model);
            Intent intent;
            intent = new Intent(LocationList.this, DetailForm.class);
            intent.putExtra("ID",recordID);
            startActivity(intent);
        }
    };

    static class LocationHolder {
        private TextView placeName = null;
        private TextView placeAddress = null;
        LocationHolder (View row) {

            placeName = (TextView)row.findViewById(R.id.placeName);
            placeAddress = (TextView)row.findViewById(R.id.placeAddress);
        }
        void populateFrom(Cursor c, TripHelper helper ) {
            placeName.setText(helper.getLocationName(c));
        }
    }

    class LocationAdapter extends CursorAdapter {
        LocationAdapter(Context context, Cursor cursor, int flags) {
            super(context, cursor, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            LocationHolder holder = (LocationHolder) view.getTag();
            holder.populateFrom(cursor, helper);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup  parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            LocationHolder holder = new LocationHolder(row);
            row.setTag(holder);
            return (row);
        }
    }
}

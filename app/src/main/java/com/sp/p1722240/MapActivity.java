package com.sp.p1722240;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sp.p1722240.FetchUrl;
import com.sp.p1722240.TaskLoadedCallback;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private Button buttonDetails;
    private Button buttonList;
    private Button buttonRoute;

    private MarkerOptions destination, me;
    private double lat;
    private double lon;
    private double myLat;
    private double myLon;
    private LatLng DESTINATION;
    private LatLng ME;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        buttonDetails = (Button) findViewById(R.id.details);
        buttonList = (Button) findViewById(R.id.list);
        buttonRoute = (Button) findViewById(R.id.direct);

        buttonList.setOnClickListener(onList);
        buttonDetails.setOnClickListener(onDetails);
        buttonRoute.setOnClickListener(onRoute);

        lat = getIntent().getDoubleExtra("LATITUDE", 0);
        lon = getIntent().getDoubleExtra("LONGITUDE", 0);
        myLat = getIntent().getDoubleExtra("MYLATITUDE", 0);
        myLon = getIntent().getDoubleExtra("MYLONGITUDE", 0);
        DESTINATION = new LatLng(1.3010,103.8396 );
        ME = new LatLng(myLat, myLon);

        DESTINATION  = new MarkerOptions().position(ME).title("DESTINATION");
        me = new MarkerOptions().position(DESTINATION).title("ME");



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.location_map);
        mapFragment.getMapAsync(this);
    }

    private View.OnClickListener onList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    private View.OnClickListener onDetails = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i;
            i = new Intent(MapActivity.this, DetailForm.class);
            startActivity(i);
        }
    };

    private View.OnClickListener onRoute = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new FetchUrl(MapActivity.this).execute(getUrl(me.getPosition(), destination.getPosition(), "driving"), "driving");
            //Toast.makeText(MapActivity.this, "Test", Toast.LENGTH_LONG).show();
        }
    };

    private String getUrl(LatLng me, LatLng destination, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + me.latitude + "," + me.longitude;
        // Destination of route
        String str_dest = "destination=" + destination.latitude + "," + destination.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        mMap.addMarker(destination);
        mMap.addMarker(me);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ME, 15));
    }
}

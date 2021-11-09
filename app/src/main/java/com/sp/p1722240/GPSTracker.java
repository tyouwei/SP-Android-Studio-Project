package com.sp.p1722240;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class GPSTracker extends Service implements LocationListener {
    private Context mContext = null;
    boolean isGPSEnabled = false;  //flag for GPS status
    boolean isNetworkEnabled = false;  //flag for network status
    boolean canGetLocation = false;  //flag for GPS status

    Location location;
    double latitude;
    double longitude;

    //Min distance to change updates in metres
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metres
    //Min time between updates in ms
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;//1 minute
    //Declare location manager
    protected LocationManager locationManager;

    public GPSTracker() {
        checkGPSPermissions();
    }

    public GPSTracker(Context context) {
        this.mContext = context;
        checkGPSPermissions();
    }

    public Location getLocation() {
        this.canGetLocation = false;
        try {
            locationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!isGPSEnabled && !isNetworkEnabled) {
                //no network provider enabled
                //prompt user to enable location services
                showEnableLocationAlert();
            }else{
                this.canGetLocation = true;
                if(isNetworkEnabled) {
                    //permission granted
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                //if GPS enabled get lat/lon using GPS service
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                        if (locationManager !=null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
        }catch (SecurityException e) {
            e.printStackTrace();
        }
        return location;
    }
    //function to get latitude
    public double getLatitude() {
        if (location != null)
            latitude = location.getLatitude();

        return latitude;
    }
    //function to get longitude
    public  double getLongitude() {
        if (location != null)
            longitude = location.getLongitude();

        return longitude;
    }
    //stop using gps listener
    //calling this function will stop gps services
    public void stopUsingGPS() {
        if (location != null)
            locationManager.removeUpdates(GPSTracker.this);
    }
    //function to check gps wifi enabled @return boolean
    public boolean canGetLocation() {
        checkGPSPermissions();
        return canGetLocation;
    }
    //check for location permission
    public void checkGPSPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionState2 = ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
        //if permission granted, get location
        if (permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED)
            getLocation();
        else
            showEnablePermissionAlert();
        //prompt user to enable location permission
    }

    //function to show settings alert dialog on pressing settings button will launch settings options
    public void showEnablePermissionAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        //Setting Dialog title
        alertDialog.setTitle("Location Permission Settings");
        //Setting Dialog message
        alertDialog.setMessage("List Location Permission is not enabled. Do you want to go to settings menu?");
        //on presing settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                //go to app setting
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        //on pressing button cancel
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //show alert message
        alertDialog.show();
    }
    public void showEnableLocationAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        //setDialog title
        alertDialogBuilder.setTitle("Location Service Settings");
        alertDialogBuilder.setMessage("Location service is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable Location Service", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //goto location service setting
                        Intent callGPSSettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert =alertDialogBuilder.create();
        alert.show();
    }
    public void onLocationChanged(Location location) {
        getLocation();
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

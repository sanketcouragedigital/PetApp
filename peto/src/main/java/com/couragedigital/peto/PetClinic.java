package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.widget.Toast;
import com.couragedigital.peto.Adapter.ClinicListAdapter;
import com.couragedigital.peto.Connectivity.PetFetchClinicList;
import com.couragedigital.peto.Connectivity.PetFetchHomeClinicList;
import com.couragedigital.peto.Listeners.PetFetchClinicListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.model.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PetClinic extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    private String url = URLInstance.getUrl();
    private ProgressDialog progressDialog;
    public List<ClinicListItems> clinicListItemsArrayList = new ArrayList<ClinicListItems>();

    static String urlForFetch;
    private int current_page = 1;

    private String userEmail;
    static int homeornearbyLocationValue;
    SessionManager sessionManager;

    private String latitudeValue;
    private String longitudeValue;
    GPSCoordinates gpsCoordinates = new GPSCoordinates();
    protected Location lastLocation;
    protected static final String TAG = "PetClinic";
    protected GoogleApiClient mGoogleApiClient;

    boolean movedgreaterthanonekm = false;
    boolean isFirstLocation = false;


    private static final int RequestLocationId = 0;
    String[] permissions =
            {
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            };

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetClinic.this.getPackageManager();
        ComponentName component = new ComponentName(PetClinic.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        else {
            mGoogleApiClient.connect();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petclinic);
        recyclerView = (RecyclerView) findViewById(R.id.petClinicList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Clinics...");
        progressDialog.show();
        buildGoogleApiClient();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        mGoogleApiClient.disconnect();
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        PetClinic.this.finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void getLocation() {

        boolean isGPSLocationSet;

        if (lastLocation != null) {
            latitudeValue = String.valueOf(lastLocation.getLatitude());
            longitudeValue = String.valueOf(lastLocation.getLongitude());

            String prevGPSlatitude = gpsCoordinates.getLatitude();
            String prevGPSlongitude = gpsCoordinates.getLongitude();

            if (prevGPSlatitude == null || prevGPSlongitude == null) {
                gpsCoordinates.setLatitude(latitudeValue);
                gpsCoordinates.setLongitude(longitudeValue);
                isGPSLocationSet = true;
                isFirstLocation = true;
            } else {
                isFirstLocation = false;
                isGPSLocationSet = true;
            }

            if (getDistanceFromPreviousLocation(latitudeValue, longitudeValue) > 1000 && isGPSLocationSet) {
                //person has moved 1 km away than his prev location. Update The GPS location.
                gpsCoordinates.setLatitude(latitudeValue);
                gpsCoordinates.setLongitude(longitudeValue);
                movedgreaterthanonekm = true;
                Toast.makeText(this, R.string.location_Changed, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    private float getDistanceFromPreviousLocation(String currentLatitudeValue, String currentLongitudeValue) {

        Location currentLocation = new Location("point A");

        currentLocation.setLatitude(Double.parseDouble(currentLatitudeValue));
        currentLocation.setLongitude(Double.parseDouble(currentLongitudeValue));

        Location previousLocation = new Location("point B");

        previousLocation.setLatitude(Double.parseDouble(gpsCoordinates.getLatitude()));
        previousLocation.setLongitude(Double.parseDouble(gpsCoordinates.getLongitude()));

        float distance = currentLocation.distanceTo(previousLocation);

        return distance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RequestLocationId) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate();
            } else {
                Toast.makeText(this, "Location Permission is Denied", Toast.LENGTH_LONG).show();
                PetClinic.this.finish();
            }
        }
    }

    private void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            homeornearbyLocationValue = extras.getInt("STATE_OF_CLICK");
        }
        if (Build.VERSION.SDK_INT >= 23 && homeornearbyLocationValue == 1) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER )) {
                buildAlertMessageNoGps();
            }
            else {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, RequestLocationId);
                } else {
                    lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    getLocation();
                }
            }
        } else if(Build.VERSION.SDK_INT < 23 && homeornearbyLocationValue == 1) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER )) {
                buildAlertMessageNoGps();
            }
            else {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                getLocation();
            }
        }

        adapter = new ClinicListAdapter(clinicListItemsArrayList);
        recyclerView.setAdapter(adapter);

        if (homeornearbyLocationValue == 0) {
            clinicListItemsArrayList.clear();
            adapter.notifyDataSetChanged();
            sessionManager = new SessionManager(getApplicationContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            String email = user.get(SessionManager.KEY_EMAIL);
            userEmail = email;
            url = url + "?method=ClinicByAddress&format=json&currentPage=" + current_page + "&email=" + userEmail + "";
            recyclerView.smoothScrollToPosition(0);
            recyclerView.addOnScrollListener(new PetFetchClinicListScrollListener(linearLayoutManager, current_page) {
                @Override
                public void onLoadMore(int current_page) {
                    url = "";
                    url = URLInstance.getUrl();
                    url = url + "?method=ClinicByAddress&format=json&currentPage=" + current_page + "&email=" + userEmail + "";
                    grabURLOfHome(url);
                }
            });
            grabURLOfHome(url);
        }
        else if (homeornearbyLocationValue == 1) {
            clinicListItemsArrayList.clear();
            adapter.notifyDataSetChanged();
            url = url + "?method=ClinicByCurrentLocation&format=json&currentPage=" + current_page + "&latitude=" + gpsCoordinates.getLatitude() + "&longitude=" + gpsCoordinates.getLongitude() + "";
            recyclerView.smoothScrollToPosition(0);
            recyclerView.addOnScrollListener(new PetFetchClinicListScrollListener(linearLayoutManager, current_page) {

                @Override
                public void onLoadMore(int current_page) {
                    url = "";
                    url = URLInstance.getUrl();
                    url = url + "?method=ClinicByCurrentLocation&format=json&currentPage=" + current_page + "&latitude=" + gpsCoordinates.getLatitude() + "&longitude=" + gpsCoordinates.getLongitude() + "";
                    grabURL(url);
                }
            });
            grabURL(url);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    private class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetFetchClinicList petFetchClinicList = new PetFetchClinicList(PetClinic.this);
                petFetchClinicList.petFetchClinicList(clinicListItemsArrayList, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    private void grabURLOfHome(String url) {
        new FetchHomeListFromServer().execute(url);
    }

    private class FetchHomeListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetFetchHomeClinicList petFetchHomeClinicList = new PetFetchHomeClinicList(PetClinic.this);
                petFetchHomeClinicList.petFetchHomeClinicList(clinicListItemsArrayList, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetClinic.this.getPackageManager();
        ComponentName component = new ComponentName(PetClinic.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}


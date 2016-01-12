package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.widget.Toast;
import com.couragedigital.petapp.Adapter.ClinicListAdapter;
import com.couragedigital.petapp.Connectivity.PetFetchClinicList;
import com.couragedigital.petapp.Connectivity.PetFetchHomeClinicList;
import com.couragedigital.petapp.Listeners.PetFetchClinicListScrollListener;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.model.*;
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

    private static String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
    private ProgressDialog progressDialog;
    public List<ClinicListItems> clinicListItemsArrayList = new ArrayList<ClinicListItems>();

    static String urlForFetch;
    private int current_page = 1;

    private String userEmail;
    int value;
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
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
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
        adapter = new ClinicListAdapter(clinicListItemsArrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        buildGoogleApiClient();
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

            if (getDistance(latitudeValue, longitudeValue) > 1000 && isGPSLocationSet) {
                //person has moved 1 km away than his prev location. Update The GPS location.
                gpsCoordinates.setLatitude(latitudeValue);
                gpsCoordinates.setLongitude(longitudeValue);
                movedgreaterthanonekm = true;
                Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }
    }

    private float getDistance(String latitudeValue, String longitudeValue) {

        Location locationA = new Location("point A");

        locationA.setLatitude(Double.parseDouble(latitudeValue));
        locationA.setLongitude(Double.parseDouble(longitudeValue));

        Location locationB = new Location("point B");

        locationB.setLatitude(Double.parseDouble(gpsCoordinates.getLatitude()));
        locationB.setLongitude(Double.parseDouble(gpsCoordinates.getLongitude()));

        float distance = locationA.distanceTo(locationB);

        return distance;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestLocationId: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //gpsCoordinates = locationDetector.getLocation();

                    getLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Loctaion Permission is Deined", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    private void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //  Toast.makeText(this, "App Required Loctaion Permission", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, permissions, RequestLocationId);
            } else {
                lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                getLocation();
            }
        } else {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            getLocation();
        }


        if (isFirstLocation || movedgreaterthanonekm) {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                value = extras.getInt("STATE_OF_CLICK");
            }

            // Showing progress dialog before making http request
            progressDialog.setMessage("Fetching List Of Clinics...");
            progressDialog.show();

            if (value == 0) {
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String email = user.get(SessionManager.KEY_EMAIL);
                userEmail = email;
                url = url + "?method=ClinicByAddress&format=json&currentPage=" + current_page + "&email=" + userEmail + "";
                recyclerView.smoothScrollToPosition(0);
                recyclerView.addOnScrollListener(new PetFetchClinicListScrollListener(linearLayoutManager, current_page) {
                    @Override
                    public void onLoadMore(int current_page) {
                        url = url + "?method=ClinicByAddress&format=json&currentPage=" + current_page + "&email=" + userEmail + "";
                        grabURLOfHome(url);
                    }
                });
                grabURLOfHome(url);
            } else if (value == 1) {
                url = url + "?method=ClinicByCurrentLocation&format=json&currentPage=" + current_page + "&latitude=" + gpsCoordinates.getLatitude() + "&longitude=" + gpsCoordinates.getLongitude() + "";
                recyclerView.smoothScrollToPosition(0);
                recyclerView.addOnScrollListener(new PetFetchClinicListScrollListener(linearLayoutManager, current_page) {

                    @Override
                    public void onLoadMore(int current_page) {
                        url = url + "?method=ClinicByCurrentLocation&format=json&currentPage=" + current_page + "&latitude=" + gpsCoordinates.getLatitude() + "&longitude=" + gpsCoordinates.getLongitude() + "";
                        grabURL(url);
                    }
                });
                grabURL(url);
            }

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
                PetFetchClinicList.petFetchClinicList(clinicListItemsArrayList, adapter, urlForFetch, progressDialog);
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
                PetFetchHomeClinicList.petFetchHomeClinicList(clinicListItemsArrayList, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }
}


package com.couragedigital.peto;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ClinicMap extends FragmentActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap mMap;
    String latitude;
    String longitude;
    Double lat;
    Double longi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_map);

        Intent intent = getIntent();
        if (null != intent) {
            latitude = intent.getStringExtra("selectedClinicLat");
            longitude = intent.getStringExtra("selectedClinicLong");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        lat = Double.parseDouble(latitude);
        longi =  Double.parseDouble(longitude);
        // Add a marker in Mumbai, India, and move the camera.
        LatLng mumbai = new LatLng(lat,longi);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

        //mMap.setMyLocationEnabled(true);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mumbai));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 13));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.addMarker(new MarkerOptions()
                .title("mumbai")
                .snippet("The most populous city in India.")
                .position(mumbai));
    }
}



//    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
//    private String title;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.clinic_map);
//
//        Bundle bundle = new Bundle(getIntent().getExtras());
//        this.title = bundle.getString("name");
//        setUpMapIfNeeded();
//        //Other lines of code
//        //...
//        //...
//    }
//
//    private void setUpMapIfNeeded() {
//        // Do a null check to confirm that we have not already instantiated the map.
//        if (mMap == null) {
//            // Try to obtain the map from the SupportMapFragment.
//            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                    .getMap();
//            // Check if we were successful in obtaining the map.
//            if (mMap != null) {
//                setUpMap();
//            }
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    private void setUpMap() {
//        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for Activity#requestPermissions for more details.
////            return;
////        }
//            mMap.setMyLocationEnabled(true);
//            //Move the map to a specific point
//
//
//        }
//    }
//}
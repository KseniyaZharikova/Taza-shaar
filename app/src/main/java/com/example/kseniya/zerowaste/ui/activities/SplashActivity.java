package com.example.kseniya.zerowaste.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.utils.Constants;
import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.kseniya.zerowaste.BuildConfig.BASE_URL_FIREBASE;


public class SplashActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private boolean isAllDataReady = false;
    private double lat;
    private double lng;
    private List<ReceptionPoint> pointList;

    @Override
    protected int getViewLayout() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadMarkers();
        if (PermissionUtils.Companion.isLocationEnable(this)) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void downloadMarkers() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReferenceFromUrl(BASE_URL_FIREBASE + Constants.FIREBASE_RECEPTION_POINTS);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pointList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ReceptionPoint point = postSnapshot.getValue(ReceptionPoint.class);
                    pointList.add(point);
                    Log.d(TAG, "onDataChange: " + pointList.size());
                }
                startActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            lat = location.getLatitude();
            lng = location.getLongitude();
            startActivity();
        });
    }

    private void startActivity() {
        if (isAllDataReady) {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra("lat", lat)
                    .putExtra("lng", lng)
                    .putExtra("reception_points", (Serializable) pointList));
            finish();
        }
        isAllDataReady = true;
    }
}
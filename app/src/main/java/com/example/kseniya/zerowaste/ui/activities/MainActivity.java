package com.example.kseniya.zerowaste.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.utils.Constants;
import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.ui.fragments.ChoseFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private MapboxMap map;
    private double lat;
    private double lng;

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.myLocation)
    ImageView myLocation;

    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMap(savedInstanceState);
        myLocation.setOnClickListener(this);
        lat = getIntent().getDoubleExtra("lat", 0);
        Log.d("MainActivity", "onCreate: " + lat);
        lng = getIntent().getDoubleExtra("lng", 0);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReferenceFromUrl("https://zerowaste-41a95.firebaseio.com/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        Mapbox.getInstance(this, Constants.MAPBOX_KEY);
        mapView.getMapAsync(this);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        MainActivity.this.map = mapboxMap;
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        cameraUpdate();

    }

    private void cameraUpdate() {
        CameraPosition position = new CameraPosition.Builder()
                .target(new LatLng(lat, lng)).zoom(16).tilt(20).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    @Override
    public void onClick(View v) {
        cameraUpdate();
        replaceFragment(new ChoseFragment());
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}

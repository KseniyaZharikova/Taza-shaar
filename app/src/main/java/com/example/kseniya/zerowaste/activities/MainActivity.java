package com.example.kseniya.zerowaste.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kseniya.zerowaste.utils.Constants;
import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.fragments.ChoseFragment;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {

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

    public void replaceFragment(Fragment fragment) {

        if (getSupportFragmentManager() == null) return;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
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

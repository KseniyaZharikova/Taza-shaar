package com.example.kseniya.zerowaste.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.ZeroWasteApp;
import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.interfaces.MainInterface;
import com.example.kseniya.zerowaste.ui.fragments.ChoseFragment;
import com.example.kseniya.zerowaste.ui.presenters.MainPresenter;
import com.example.kseniya.zerowaste.utils.Constants;
import com.example.kseniya.zerowaste.utils.PermissionUtils;
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

import static com.example.kseniya.zerowaste.BuildConfig.MAP_BOX_KEY;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, MainInterface.View {

    private final String TAG = getClass().getSimpleName();
    private MainInterface.Presenter mainPresenter;
    private MapboxMap map;
    private double lat;
    private double lng;
    private List<ReceptionPoint> mPoints;

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

        mainPresenter = new MainPresenter(this);
        mainPresenter.bind(this);
        mainPresenter.getCurrentLocation(this);
        initMap(savedInstanceState);
        myLocation.setOnClickListener(this);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
        mPoints = (List<ReceptionPoint>) getIntent().getSerializableExtra("reception_points");

        mainPresenter.startLocationUpdates();
    }

    private void initMap(Bundle savedInstanceState) {
        Mapbox.getInstance(this, MAP_BOX_KEY);
        mapView.getMapAsync(this);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void drawReceptionPoints() {
     //   List<ReceptionPoint> list = mainPresenter.getPointFromDatabase();

        for (int i = 0; i < mPoints.size(); i++) {
            map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(mPoints.get(i).getLatitude()), Double.parseDouble(mPoints.get(i).getLongitude()))));
        }

    }

    @Override
    public void startActivity(Double lat, Double lng, List<ReceptionPoint> pointList) {

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        MainActivity.this.map = mapboxMap;
        showMarkers(lat, lng);
        cameraUpdate();
        drawReceptionPoints();
        replaceFragment(new ChoseFragment());

    }

    private void cameraUpdate() {
        if (lat == 0.0) {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(Constants.LAT, Constants.LNG)).zoom(12).tilt(14).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        }else {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(lat, lng)).zoom(16).tilt(20).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        }

    }

    @Override
    public void onClick(View v) {
        cameraUpdate();

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

    public void showMarkers(Double lat, Double lng) {
        if (PermissionUtils.Companion.isLocationEnable(this)) {
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
        }
    }
}
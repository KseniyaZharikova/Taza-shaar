package com.example.kseniya.zerowaste.ui.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.ZeroWasteApp;
import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface;
import com.example.kseniya.zerowaste.interfaces.MainInterface;
import com.example.kseniya.zerowaste.ui.fragments.ChoseFragment;
import com.example.kseniya.zerowaste.ui.presenters.MainPresenter;
import com.example.kseniya.zerowaste.utils.Constants;
import com.example.kseniya.zerowaste.utils.PermissionUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.offline.OfflineManager;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.kseniya.zerowaste.BuildConfig.MAP_BOX_KEY;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, MainInterface.View, CheckBoxInterface {

    private final String TAG = getClass().getSimpleName();
    private MainInterface.Presenter mainPresenter;
    private MapboxMap map;
    private double lat;
    private double lng;
    List<Marker> mMarkerList;
    private OfflineManager mOfflineManager;
    public static final String JSON_CHARSET = "UTF-8";
    public static final String JSON_FIELD_REGION_NAME = "BISHKEK";

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

        mainPresenter = new MainPresenter(ZeroWasteApp.get(this).getDatabase());
        mainPresenter.bind(this);
        mainPresenter.getCurrentLocation(this);
        initMap(savedInstanceState);
        myLocation.setOnClickListener(this);
        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);
//        mPoints = (List<ReceptionPoint>) getIntent().getSerializableExtra("reception_points");

        mainPresenter.startLocationUpdates();
    }

    private void initMap(Bundle savedInstanceState) {
        Mapbox.getInstance(this, MAP_BOX_KEY);
        mapView.getMapAsync(this);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public void drawReceptionPoints(List<ReceptionPoint> pointFromDatabase) {
        Icon icon = IconFactory.getInstance(this).fromResource(R.drawable.other_color_marker);
        mMarkerList = new ArrayList<>();
        for (int i = 0; i < pointFromDatabase.size(); i++) {
            Marker marker =  map.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(pointFromDatabase.get(i).getLatitude()), Double.parseDouble(pointFromDatabase.get(i).getLongitude())))
                    .icon(icon));
            mMarkerList.add(marker);
        }
    }

    @Override
    public void showFilteredReceptionPoints(List<ReceptionPoint> list) {
        for (int i = 0; i < mMarkerList.size(); i++) {
            map.removeMarker(mMarkerList.remove(i));
        }
        mMarkerList.clear();
        drawReceptionPoints(list);
    }

    @Override
    public void startActivity(Double lat, Double lng, List<ReceptionPoint> pointList) {

    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        Log.d(TAG, "onMapReady: ready");
        mOfflineManager = OfflineManager.getInstance(MainActivity.this);

//        downloadOfflineBishkek(mapboxMap);
        MainActivity.this.map = mapboxMap;
        showMarkers(lat, lng);
        cameraUpdate();
        drawReceptionPoints(mainPresenter.getPointFromDatabase());
        replaceFragment(new ChoseFragment());

    }

//    private void downloadOfflineBishkek(MapboxMap mapboxMap){
//        LatLngBounds latLngBounds = new LatLngBounds.Builder()
//                .include(new LatLng(Constants.LAT_NE, Constants.LNG_NE)) // Northeast
//                .include(new LatLng(Constants.LAT_SW, Constants.LNG_SW)) // Southwest
//                .build();
//
//        OfflineTilePyramidRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
//                mapboxMap.getStyleUrl(),
//                latLngBounds,
//                10,
//                20,
//                MainActivity.this.getResources().getDisplayMetrics().density);
//
//        // Set the metadata
//        byte[] metadata;
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(JSON_FIELD_REGION_NAME, "Yosemite National Park");
//            String json = jsonObject.toString();
//            metadata = json.getBytes(JSON_CHARSET);
//        } catch (Exception exception) {
//            Log.e(TAG, "Failed to encode metadata: " + exception.getMessage());
//            metadata = null;
//        }
//
//        mOfflineManager.createOfflineRegion(definition, Objects.requireNonNull(metadata), new OfflineManager.CreateOfflineRegionCallback() {
//            @Override
//            public void onCreate(OfflineRegion offlineRegion) {
//                offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
//
//                offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
//                    @Override
//                    public void onStatusChanged(OfflineRegionStatus status) {
//                        Log.d(TAG, "onStatusChanged: status" + status.getCompletedResourceCount());
//
//
//                        if (status.isComplete()) Log.d(TAG, "onStatusChanged: complete");
//                    }
//
//                    @Override
//                    public void onError(OfflineRegionError error) {
//                        Log.d(TAG, "onError: " + error.getMessage());
//                    }
//
//                    @Override
//                    public void mapboxTileCountLimitExceeded(long limit) {
//                        Log.e(TAG, "Mapbox tile count limit exceeded: " + limit);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.d(TAG, "onError: " + error);
//            }
//        });
//
//    }

    private void cameraUpdate() {
        if (lat == 0.0) {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(Constants.LAT, Constants.LNG)).zoom(12).tilt(14).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        } else {
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

    @Override
    public void onCheckBoxClicked(int tag) {
        mainPresenter.setCheckedPoints(tag);
    }
}
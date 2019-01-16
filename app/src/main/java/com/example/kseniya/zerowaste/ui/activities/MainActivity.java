package com.example.kseniya.zerowaste.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.ZeroWasteApp;
import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.interfaces.CheckBoxInterface;
import com.example.kseniya.zerowaste.interfaces.MainInterface;
import com.example.kseniya.zerowaste.interfaces.SortedList;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.kseniya.zerowaste.BuildConfig.MAP_BOX_KEY;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, MainInterface.View, CheckBoxInterface, MapboxMap.OnMarkerClickListener {

    private final String TAG = getClass().getSimpleName();
    private MainInterface.Presenter mainPresenter;
    private MapboxMap map;
    private Marker marker;
    private List<Marker> mMarkerList = new ArrayList<>();

    @BindView(R.id.mapView)
    MapView mapView;

    @BindView(R.id.myLocation)
    ImageView myLocation;

    @BindView(R.id.mainActivityRelativeLayout)
    RelativeLayout mainActivityRelativeLayout;


    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainPresenter = new MainPresenter(ZeroWasteApp.get(this).getSqLiteHelper());
        mainPresenter.bind(this);
        initMap(savedInstanceState);
        mainPresenter.getPermission(this);
        myLocation.setVisibility(View.INVISIBLE);
        getHeight();
    }

    private void initMap(Bundle savedInstanceState) {
        Mapbox.getInstance(this, MAP_BOX_KEY);
        mapView.getMapAsync(this);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
    }
    public static Icon drawableToIcon(@NonNull Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
              vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    @SuppressLint("NewApi")
    @Override
    public void drawReceptionPoints(List<ReceptionPoint> pointFromDatabase) {
        for (int i = 0; i < pointFromDatabase.size(); i++) {
            Marker marker = map.addMarker(new MarkerOptions()
                  .position(new LatLng(Double.parseDouble(pointFromDatabase.get(i).getLatitude()), Double.parseDouble(pointFromDatabase.get(i).getLongitude())))
                  .icon( drawableToIcon(this,Constants.PointsType(pointFromDatabase.get(i).getType()))));
            mMarkerList.add(marker);
        }
    }

    @Override
    public void clearAllMarkersAndDrawNew(List<ReceptionPoint> list) {
        for (int i = 0; i < mMarkerList.size(); i++) {
            map.removeMarker(mMarkerList.get(i));
        }
        Log.d(TAG, "showFilteredReceptionPoints11: " + map.getMarkers().size());
        mMarkerList.clear();
        drawReceptionPoints(list);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    mainPresenter.getCurrentLocation(this);
                    myLocation.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        MainActivity.this.map = mapboxMap;
        replaceFragment(new ChoseFragment());
        map.setOnMarkerClickListener(this);
        myLocation.setOnClickListener(this);
        cameraUpdate(Constants.LAT, Constants.LNG);
        if (PermissionUtils.Companion.isLocationEnable(this)) {
            mainPresenter.startLocationUpdates();
            myLocation.setVisibility(View.VISIBLE);
        }


    }


    public void cameraUpdate(double lat, double lng) {
        if (map != null) {
            Log.d("Loca_cameraUpdate", String.valueOf(lat + " " + lng));
            CameraPosition position = new CameraPosition.Builder()
                  .target(new LatLng(lat, lng)).zoom(13).tilt(15).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(position));


        }
    }


    public void showMyCurrentLocation(Double lat, Double lng) {
        Log.d("Loca_showMarkers", String.valueOf(lat + " " + lng));
        if (marker != null)
            map.removeMarker(marker);
        if (PermissionUtils.Companion.isLocationEnable(this) && map != null) {
            marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));

        }
    }


    @Override
    public void onCheckBoxClicked(int tag) {
        mainPresenter.setCheckedPoints(tag);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int pos = mMarkerList.lastIndexOf(marker);
        if (pos > -1) {
            cameraUpdate(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
            showItemByClickMarker(mainPresenter.getCurrentPoint(pos));
        }
        return false;
    }

    @Override
    public void showAllPoints() {
        SortedList.list.clear();
        clearAllMarkersAndDrawNew(mainPresenter.getPointFromDatabase());
    }

    @Override
    public void onClick(View v) {
        mainPresenter.getCurrentLocation(this);

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
    public void startActivity() {

    }

    @Override
    public void dialogNoInternet() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void zoomCameraToMarker(@NotNull ReceptionPoint item) {
        List<ReceptionPoint> list = new ArrayList<>();
        list.add(item);
        clearAllMarkersAndDrawNew(list);
        cameraUpdate(Double.parseDouble(item.getLatitude()), Double.parseDouble(item.getLongitude()));
    }


    public void getHeight() {

        final ViewTreeObserver observer= mainActivityRelativeLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Constants.HIGHT_OF_ACTIVITY = mainActivityRelativeLayout.getHeight();
            }
        });

    }

    @Override
    public void drawPointsByType() {
        clearAllMarkersAndDrawNew(SortedList.list);
    }
}
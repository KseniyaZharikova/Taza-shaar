package com.example.kseniya.zerowaste.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.kseniya.zerowaste.R;
import com.example.kseniya.zerowaste.ZeroWasteApp;
import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.interfaces.MainInterface;
import com.example.kseniya.zerowaste.ui.presenters.MainPresenter;
import com.example.kseniya.zerowaste.utils.Constants;

import java.io.Serializable;
import java.util.List;


public class SplashActivity extends BaseActivity implements MainInterface.View {

    private boolean isAllDataReady = false;
    private MainInterface.Presenter mainPresenter;

    @Override
    protected int getViewLayout() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainPresenter = new MainPresenter(ZeroWasteApp.get(this).getDatabase());
        mainPresenter.bind(this);
        mainPresenter.getPermission(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.LOCATION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED) {
                    mainPresenter.getCurrentLocation(this);
                }
            }

        }
        mainPresenter.downloadMarkers();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void startActivity(Double lat, Double lng, List<ReceptionPoint> pointList) {

        startActivity(new Intent(this, MainActivity.class)
                .putExtra("lat", lat)
                .putExtra("lng", lng)
                .putExtra("reception_points", (Serializable) pointList));
        finish();

    }


    @Override
    public void showMarkers(Double lat, Double lng) {

    }

    @Override
    public void drawReceptionPoints(List<ReceptionPoint> pointFromDatabase) {
    }

    @Override
    public void showFilteredReceptionPoints(List<ReceptionPoint> list) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.unbind();
    }
}
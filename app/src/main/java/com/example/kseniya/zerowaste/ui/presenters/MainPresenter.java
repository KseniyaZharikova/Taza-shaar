package com.example.kseniya.zerowaste.ui.presenters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.kseniya.zerowaste.data.ReceptionPoint;
import com.example.kseniya.zerowaste.data.db.SQLiteHelper;
import com.example.kseniya.zerowaste.interfaces.MainInterface;
import com.example.kseniya.zerowaste.interfaces.SortedList;
import com.example.kseniya.zerowaste.utils.ConnectionUtils;
import com.example.kseniya.zerowaste.utils.Constants;
import com.example.kseniya.zerowaste.utils.PermissionUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.kseniya.zerowaste.BuildConfig.BASE_URL_FIREBASE;

public class MainPresenter implements MainInterface.Presenter, LocationListener {
    private MainInterface.View mainView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double lat = Constants.LAT;
    private double lng = Constants.LNG;
    private SQLiteHelper db;

    private final String TAG = getClass().getSimpleName();


    private List<ReceptionPoint> pointList;

    public MainPresenter(SQLiteHelper sqLiteHelper) {
        db = sqLiteHelper;
        pointList = getPointFromDatabase();
    }

    @Override
    public void getPermission(Activity activity) {
        if (PermissionUtils.Companion.isLocationEnable(activity)) {
            getCurrentLocation(activity);
        }
    }

    @Override
    public void checkNetwork(Activity activity) {
        if (ConnectionUtils.isHasNetwork(activity.getApplicationContext())) {
            downloadMarkers();
        } else if (db.getReceptionPoints().size() != 0) {
            mainView.startActivity();
        } else {
            mainView.dialogNoInternet();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void getCurrentLocation(Activity activity) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

            lat = location.getLatitude();
            lng = location.getLongitude();
            mainView.cameraUpdate(lat, lng);
        });


    }

    @Override
    public void downloadMarkers() {
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
                saveMarkersToDb();

                mainView.startActivity();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveMarkersToDb() {
        db.deleteReceptionPoints();
        db.saveReceptionPoints(pointList);
    }

    @Override
    public ReceptionPoint getCurrentPoint(int position) {
        if (SortedList.list.size() != 0) {
            return SortedList.list.get(position);
        }
        return pointList.get(position);
    }

    @Override
    public void bind(MainInterface.View view) {
        mainView = view;
    }

    @Override
    public void unbind() {
        mainView = null;
    }


    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {

        int INTERVAL_UPDATES = 5000;
        int MINIMUM_INTERVAL_UPDATES = 1000;

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(INTERVAL_UPDATES);
        locationRequest.setFastestInterval(MINIMUM_INTERVAL_UPDATES);
        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback,
                Looper.myLooper());
    }

    @Override
    public void setCheckedPoints(int category) {
        List<ReceptionPoint> list = new ArrayList<>();
        for (int i = 0; i < pointList.size(); i++) {
            if (pointList.get(i).getType() == category)
                list.add(new ReceptionPoint(pointList.get(i)));
        }
//        Log.d(TAG, "showFilteredReceptionPoints11: " + pointList.size());
//        Log.d(TAG, "showFilteredReceptionPoints11: " + SortedList.list.size());
        mainView.clearAllMarkersAndDrawNew(list);
        SortedList.list.clear();
        SortedList.list.addAll(list);
        Log.d(TAG, "setCheckedPoints: " + list.size());
    }

    @Override
    public List<ReceptionPoint> getPointFromDatabase() {
        if (pointList != null)
            pointList.clear();
        Log.d(TAG, "getPointFromDatabase: ");
        return pointList = db.getReceptionPoints();
    }


    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();

            mainView.cameraUpdate(location.getLatitude(), location.getLongitude());
            Log.d(TAG, "onLocationChanged: " + locationResult.getLastLocation().getLongitude() + " " + locationResult.getLastLocation().getLatitude());
        }
    };

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Loca_onLocationChanged", String.valueOf(location.getLatitude() + " " + location.getLongitude()));


    }

}

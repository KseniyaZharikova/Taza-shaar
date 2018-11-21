package com.example.kseniya.zerowaste;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class SplashActivity extends AppCompatActivity {
	private LocationManager locationManager;
	Location location;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {

			showLocation(location);
			return;
		}

		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);

	}

	@SuppressLint("MissingPermission")
	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (locationManager != null) locationManager.removeUpdates(locationListener);
	}

	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {

			showLocation(location);

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@SuppressLint("MissingPermission")
		@Override
		public void onProviderEnabled(String provider) {

			showLocation(locationManager.getLastKnownLocation(provider));
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 10) {
			showLocation(location);
		}
	}

	@SuppressLint("MissingPermission")
	public void showLocation(Location location) {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				1000 * 10, 10, locationListener);
	//	Log.d("SplashActivity", "onCreate: " + location.getLatitude());

		//if (location != null) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//			intent.putExtra("lat", location.getLatitude());
//			intent.putExtra("lng", location.getLongitude());
			startActivity(intent);
			finish();
		//}
	}
}
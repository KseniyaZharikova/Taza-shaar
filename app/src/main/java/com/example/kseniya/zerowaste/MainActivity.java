package com.example.kseniya.zerowaste;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

	private MapView mapView;
	private MapboxMap map;
	private double lat;
	private double lng;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mapbox.getInstance(this, "pk.eyJ1Ijoia3Nlbml5YXpoYXJpa292YSIsImEiOiJjam8wYTZsNzAwa203M3JzNm1lM2F4OHZ6In0.1LdT5jh9WRGM17wra1KrwA");
		setContentView(R.layout.activity_main);
		mapView = findViewById(R.id.mapView);
		mapView.getMapAsync(this);
		mapView.setStyleUrl(Style.MAPBOX_STREETS);
		mapView.onCreate(savedInstanceState);
		lat = getIntent().getDoubleExtra("lat", 0);
		Log.d("MainActivity", "onCreate: " + lat);
		lng = getIntent().getDoubleExtra("lng", 0);
	}

	@Override
	public void onMapReady(MapboxMap mapboxMap) {
		MainActivity.this.map = mapboxMap;
		map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
		cameraUpdate();

	}
	private  void cameraUpdate(){
		CameraPosition position = new CameraPosition.Builder()
				.target(new LatLng(lat, lng)).zoom(16).tilt(20).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
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

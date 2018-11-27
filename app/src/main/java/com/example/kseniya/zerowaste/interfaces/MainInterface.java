package com.example.kseniya.zerowaste.interfaces;

import android.app.Activity;

import com.example.kseniya.zerowaste.data.ReceptionPoint;

import java.util.List;

public interface MainInterface {
	interface View {

		void showMarkers(Double lat, Double lng);

		void drawReceptionPoints();

		void startActivity(Double lat, Double lng, List<ReceptionPoint> pointList);

	}

	interface Presenter extends LifeCycle<View> {

		void downloadMarkers();

		void getCurrentLocation(Activity activity);

		void getPermission(Activity activity);

		void startLocationUpdates();

		List<ReceptionPoint>  getPointFromDatabase();
	}

}

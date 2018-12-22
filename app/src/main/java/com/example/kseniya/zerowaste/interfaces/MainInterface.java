package com.example.kseniya.zerowaste.interfaces;

import android.app.Activity;

import com.example.kseniya.zerowaste.data.ReceptionPoint;

import java.util.List;

public interface MainInterface {
	interface View {

		void showMarkers(Double lat, Double lng);

		void drawReceptionPoints(List<ReceptionPoint> pointFromDatabase);

		void showFilteredReceptionPoints(List<ReceptionPoint> list);

		void startActivity(Double lat, Double lng);

		void dialogNoInternet();

	}

	interface Presenter extends LifeCycle<View> {

		void downloadMarkers();

		void getCurrentLocation(Activity activity);

      void checkNetwork(Activity activity);

		void getPermission(Activity activity);

		void startLocationUpdates();

		void setCheckedPoints(int category);

		List<ReceptionPoint>  getPointFromDatabase();
	}

}

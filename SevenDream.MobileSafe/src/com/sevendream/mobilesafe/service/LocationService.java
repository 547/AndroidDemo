package com.sevendream.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private SharedPreferences msp;
	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	public void onCreate() {
		super.onCreate();
		msp = getSharedPreferences("config", MODE_PRIVATE);
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = lm.getBestProvider(criteria, true);

		listener = new MyLocationListener();
		lm.requestLocationUpdates(bestProvider, 0, 0, listener);

	}

	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			msp.edit()
					.putString(
							"location",
							"经度:" + location.getLongitude() + ";纬度:"
									+ location.getLongitude()).commit();
			stopSelf();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	public void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(listener);

	}

}

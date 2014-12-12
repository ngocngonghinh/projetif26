package info.androidhive.info;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import controler.GPSTracker;

public class MapActivity extends Activity {

	// Google Map
	private GoogleMap googleMap;
	Timer timer;
	TimerTask timerTask;
	// we are going to use a handler to be able to run in our TimerTask
	final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		try {
			// Loading map
			initilizeMap();
			// LocationManager mlocManager =
			//
			// (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			//
			// LocationListener mlocListener = new MyLocationListener();
			//
			// mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// 0,
			// 0, mlocListener);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}

		// Enable MyLocation Layer of Google Map
		googleMap.setMyLocationEnabled(true);

		// set map type
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		// onResume we start our timer so it can start when the app comes from
		// the background
		startTimer();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	// Conversion from kilometers to meters
	private static final int METERS_PER_KILOMETER = 1000;
	Double radius = (double) 1;

	private void setUpMap() {
		googleMap.clear();
		// LatLng latLng = getLocation();
		// create class object
		GPSTracker gps = new GPSTracker(this);
		LatLng latLng;
		// check if GPS enabled
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			// Create a LatLng object for the current location
			latLng = new LatLng(latitude, longitude);
			Toast.makeText(
					getApplicationContext(),
					"Your Location is - \nLat: " + latitude + "\nLong: "
							+ longitude, Toast.LENGTH_LONG).show();
			// Show the current location in Google Map
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
			CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(
					latLng, 12);
			// Zoom in the Google Map
			googleMap.animateCamera(yourLocation);
			addMarker(latLng);
			Circle mapCircle = googleMap.addCircle(new CircleOptions().center(
					latLng).radius(radius * METERS_PER_KILOMETER));
			int baseColor = Color.DKGRAY;
			mapCircle.setStrokeColor(baseColor);
			mapCircle.setStrokeWidth(2);
			mapCircle.setFillColor(Color.argb(50, Color.red(baseColor),
					Color.green(baseColor), Color.blue(baseColor)));
			updateLocation(latLng);
			getFriendsOnMap();
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

	}

	public void addMarker(LatLng latLng) {
		// create marker
		MarkerOptions marker = new MarkerOptions().position(latLng).title(
				"Hello Maps ");
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar));
		googleMap.addMarker(marker);
	}

	public void updateLocation(LatLng latLng) {
		// TODO
	}

	public void getFriendsOnMap() {
		// TODO
	}

	public void startTimer() {
		// set a new Timer
		timer = new Timer();
		// initialize the TimerTask's job
		initializeTimerTask();
		// schedule the timer, after the first 5000ms the TimerTask will run
		// every 10000ms
		timer.schedule(timerTask, 5000, 100000); //
	}

	public void stoptimertask(View v) {
		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public void initializeTimerTask() {
		timerTask = new TimerTask() {
			public void run() {
				// use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					public void run() {
						setUpMap();
					}
				});
			}
		};
	}
	// public LatLng getLocation() {
	// // Get LocationManager object from System Service LOCATION_SERVICE
	// LocationManager locationManager = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	//
	// // Create a criteria object to retrieve provider
	// Criteria criteria = new Criteria();
	//
	// // Get the name of the best provider
	// String provider = locationManager.getBestProvider(criteria, true);
	// Log.i("provider", provider);
	// // Get Current Location
	// Location myLocation = locationManager.getLastKnownLocation(provider);
	// Log.i("myLocation", myLocation.toString());
	// // Get latitude of the current location
	// double latitude = myLocation.getLatitude();
	//
	// // Get longitude of the current location
	// double longitude = myLocation.getLongitude();
	//
	// // Create a LatLng object for the current location
	// LatLng latLng = new LatLng(latitude, longitude);
	// return latLng;
	// }

	// /* Class My Location Listener */
	//
	// public class MyLocationListener implements LocationListener
	//
	// {
	//
	// @Override
	// public void onLocationChanged(Location loc)
	//
	// {
	//
	// // latitude and longitude
	// double latitude = loc.getLatitude();
	// double longitude = loc.getLongitude();
	//
	// // create marker
	// MarkerOptions marker = new MarkerOptions().position(
	// new LatLng(latitude, longitude)).title("Hello Maps ");
	//
	// // Changing marker icon
	// // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar));
	// // adding marker
	// googleMap.addMarker(marker);
	// LatLng coordinate = new LatLng(latitude, longitude);
	// CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(
	// coordinate, 5);
	// googleMap.animateCamera(yourLocation);
	// }
	//
	// @Override
	// public void onProviderDisabled(String provider)
	//
	// {
	//
	// Toast.makeText(getApplicationContext(),
	//
	// "Gps Disabled",
	//
	// Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// @Override
	// public void onProviderEnabled(String provider)
	//
	// {
	//
	// Toast.makeText(getApplicationContext(),
	//
	// "Gps Enabled",
	//
	// Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// @Override
	// public void onStatusChanged(String provider, int status, Bundle extras)
	//
	// {
	//
	// }
	//
	// }/* End of Class MyLocationListener */

}/* End of UseGps Activity */

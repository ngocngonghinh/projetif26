package utt.fr.if26.project.pages.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.User;
import utt.fr.if26.project.pages.FriendsPage;
import utt.fr.if26.project.pages.LoginPage;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.GPSTracker;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import utt.fr.if26.project.support.LocalStorage;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTab extends SherlockFragment {

	private SupportMapFragment fragment;
	private GoogleMap googleMap;

	// Google Map
	// private GoogleMap googleMap;
	public Timer timer;
	public TimerTask timerTask;
	public static final String FRIENDSMAP = "friendsMap";
	public JSONParser jsonParser = new JSONParser();
	// We are going to use a handler to be able to run in our TimerTask
	final Handler handler = new Handler();
	Location currentLocation = new Location("Current location");
	Location lastLocation = new Location("Last location");

	public static Double radius = (double) 10;
	public static int mapType = GoogleMap.MAP_TYPE_NORMAL;

	private static ArrayList<User> listUsers;
	private Button showFriendsBtn;

	/**
	 * Initialize this frag
	 * 
	 * @return
	 */
	public static MapTab newIntance() {
		MapTab frag = new MapTab();
		return frag;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
		fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (fragment == null) {
			fragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map, fragment).commit();
		}
	}

	/**
	 * Intitialize this frag's view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.map_tab, container, false);
	}

	/**
	 * Initialize all visual components of this frag's view
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.showFriendsBtn = (Button) view.findViewById(R.id.showFriendsBtn);
		this.showFriendsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getSherlockActivity(), FriendsPage.class);
				if (listUsers == null) {
					listUsers = new ArrayList<User>();
				}

				Bundle mBundle = new Bundle();
				mBundle.putSerializable(Keys.FRIENDS_LIST, getListUsers());
				i.putExtras(mBundle);
				startActivity(i);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		initilizeMap();
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = fragment.getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getSherlockActivity(), getString(R.string.mapNotEnabled), Toast.LENGTH_SHORT).show();
			}
		}
		// Enable MyLocation Layer of Google Map
		googleMap.setMyLocationEnabled(true);

		// set map type
		if (!(LocalStorage.getIntData(getSherlockActivity(), Keys.SAVING_MAP_STATE) == -1)) {
			mapType = LocalStorage.getIntData(getSherlockActivity(), Keys.SAVING_MAP_STATE);
		}
		googleMap.setMapType(mapType);
	}

	private void setUpMap() {
		googleMap.clear();
		// LatLng latLng = getLocation();
		// create class object
		GPSTracker gps = new GPSTracker(getSherlockActivity());
		// check if GPS enabled
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			// Create a currentLocation object for the current location
			LatLng latLng = new LatLng(latitude, longitude);
			currentLocation.setLatitude(latitude);
			currentLocation.setLongitude(longitude);
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			int baseColor = Color.DKGRAY;
			googleMap.addCircle(new CircleOptions().center(new LatLng(latitude, longitude)).radius(radius * Keys.METERS_PER_KILOMETER).strokeWidth(2).strokeColor(Color.DKGRAY).fillColor(Color.argb(50, Color.red(baseColor), Color.green(baseColor), Color.blue(baseColor))));
			LatLngBounds bounds = boundsWithCenterAndLatLngDistance(new LatLng(latitude, longitude), radius.floatValue() * 20, radius.floatValue() * 20);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12));
			googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 3));
			addMarker(new LatLng(latitude, longitude));

			updateLocation(longitude, latitude);
			lastLocation = currentLocation;
			getFriendsOnMap();
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

	}

	private static final double ASSUMED_INIT_LATLNG_DIFF = 1.0;
	private static final float ACCURACY = 0.01f;

	public static LatLngBounds boundsWithCenterAndLatLngDistance(LatLng center, float latDistanceInMeters, float lngDistanceInMeters) {
		latDistanceInMeters /= 2;
		lngDistanceInMeters /= 2;
		LatLngBounds.Builder builder = LatLngBounds.builder();
		float[] distance = new float[1];
		{
			boolean foundMax = false;
			double foundMinLngDiff = 0;
			double assumedLngDiff = ASSUMED_INIT_LATLNG_DIFF;
			do {
				Location.distanceBetween(center.latitude, center.longitude, center.latitude, center.longitude + assumedLngDiff, distance);
				float distanceDiff = distance[0] - lngDistanceInMeters;
				if (distanceDiff < 0) {
					if (!foundMax) {
						foundMinLngDiff = assumedLngDiff;
						assumedLngDiff *= 2;
					} else {
						double tmp = assumedLngDiff;
						assumedLngDiff += (assumedLngDiff - foundMinLngDiff) / 2;
						foundMinLngDiff = tmp;
					}
				} else {
					assumedLngDiff -= (assumedLngDiff - foundMinLngDiff) / 2;
					foundMax = true;
				}
			} while (Math.abs(distance[0] - lngDistanceInMeters) > lngDistanceInMeters * ACCURACY);
			LatLng east = new LatLng(center.latitude, center.longitude + assumedLngDiff);
			builder.include(east);
			LatLng west = new LatLng(center.latitude, center.longitude - assumedLngDiff);
			builder.include(west);
		}
		{
			boolean foundMax = false;
			double foundMinLatDiff = 0;
			double assumedLatDiffNorth = ASSUMED_INIT_LATLNG_DIFF;
			do {
				Location.distanceBetween(center.latitude, center.longitude, center.latitude + assumedLatDiffNorth, center.longitude, distance);
				float distanceDiff = distance[0] - latDistanceInMeters;
				if (distanceDiff < 0) {
					if (!foundMax) {
						foundMinLatDiff = assumedLatDiffNorth;
						assumedLatDiffNorth *= 2;
					} else {
						double tmp = assumedLatDiffNorth;
						assumedLatDiffNorth += (assumedLatDiffNorth - foundMinLatDiff) / 2;
						foundMinLatDiff = tmp;
					}
				} else {
					assumedLatDiffNorth -= (assumedLatDiffNorth - foundMinLatDiff) / 2;
					foundMax = true;
				}
			} while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
			LatLng north = new LatLng(center.latitude + assumedLatDiffNorth, center.longitude);
			builder.include(north);
		}
		{
			boolean foundMax = false;
			double foundMinLatDiff = 0;
			double assumedLatDiffSouth = ASSUMED_INIT_LATLNG_DIFF;
			do {
				Location.distanceBetween(center.latitude, center.longitude, center.latitude - assumedLatDiffSouth, center.longitude, distance);
				float distanceDiff = distance[0] - latDistanceInMeters;
				if (distanceDiff < 0) {
					if (!foundMax) {
						foundMinLatDiff = assumedLatDiffSouth;
						assumedLatDiffSouth *= 2;
					} else {
						double tmp = assumedLatDiffSouth;
						assumedLatDiffSouth += (assumedLatDiffSouth - foundMinLatDiff) / 2;
						foundMinLatDiff = tmp;
					}
				} else {
					assumedLatDiffSouth -= (assumedLatDiffSouth - foundMinLatDiff) / 2;
					foundMax = true;
				}
			} while (Math.abs(distance[0] - latDistanceInMeters) > latDistanceInMeters * ACCURACY);
			LatLng south = new LatLng(center.latitude - assumedLatDiffSouth, center.longitude);
			builder.include(south);
		}
		return builder.build();
	}

	public void addMarker(LatLng latLng) {
		// create marker
		MarkerOptions marker = new MarkerOptions().position(latLng).title(getString(R.string.mapWelcome));
		marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.avatar));
		googleMap.addMarker(marker);
	}

	public void updateLocation(Double longitude, Double latitude) {
		new UpdateLocation(longitude, latitude).execute();
	}

	public void getFriendsOnMap() {
		new GetLocations().execute();
	}

	public void startTimer() {
		// set a new Timer
		timer = new Timer();
		// initialize the TimerTask's job
		initializeTimerTask();
		// schedule the timer, after the first 2000ms the TimerTask will run
		// every 5000ms
		timer.schedule(timerTask, 2000, 50000);
	}

	public void stoptimertask() {
		// stop the timer, if it's not already null
		if (handler != null) {
			handler.removeCallbacksAndMessages(null);
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	public Runnable mRunnable = new Runnable() {
		public void run() {
			setUpMap();
		}
	};

	public void initializeTimerTask() {
		timerTask = new TimerTask() {
			public void run() {
				handler.removeCallbacks(mRunnable);
				// use a handler to run a toast that shows the current timestamp
				handler.post(mRunnable);
			}
		};
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stoptimertask();
	}

	public void updateUserInMap(User user) {
		Location locationUser = new Location("Location of User");
		locationUser.setLatitude(user.getLatitude());
		locationUser.setLongitude(user.getLongtitude());
		if (currentLocation.distanceTo(locationUser) < radius * Keys.METERS_PER_KILOMETER) {
			if (!listUsers.contains(user)) {
				listUsers.add(user);
			}
			LatLng latlng = new LatLng(user.getLatitude(), user.getLongtitude());
			// Create marker
			MarkerOptions marker = new MarkerOptions().position(latlng).title(user.getName());
			googleMap.addMarker(marker);
		}

	}

	public class UpdateLocation extends AsyncTask<String, Void, String> {
		Double longtitude;
		Double latitude;

		public UpdateLocation(Double longtitude, Double latitude) {
			this.longtitude = longtitude;
			this.latitude = latitude;
		}

		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.LONGITUDE, Double.toString(longtitude)));
			params.add(new BasicNameValuePair(Keys.LATITUDE, Double.toString(latitude)));

			// getting JSON Object
			String jsonObj = jsonParser.makeHttpRequest(Keys.UPDATE_LOCATION_URL, Keys.GET, params);
			return jsonObj;
		}
	}

	public class GetLocations extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));

			// getting JSON Object
			String jsonObj = jsonParser.makeHttpRequest(Keys.GET_LOCATION_URL, Keys.GET, params);
			return jsonObj;
		}

		@Override
		protected void onPostExecute(String result) {
			JSONArray users;
			try {
				JSONObject jObj = new JSONObject(result);
				users = jObj.getJSONArray("users");
				listUsers = User.getListUsers(users);
				for (int i = 0; i < listUsers.size(); i++) {
					updateUserInMap(listUsers.get(i));
				}
				super.onPostExecute(result);
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	public static ArrayList<User> getListUsers() {
		return listUsers;
	}
}

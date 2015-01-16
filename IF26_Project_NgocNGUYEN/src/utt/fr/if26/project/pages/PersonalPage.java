package utt.fr.if26.project.pages;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import utt.fr.if26.project.R;
import utt.fr.if26.project.pages.tabs.MapTab;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.GPSTracker;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import utt.fr.if26.project.support.LocalStorage;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class PersonalPage extends SherlockFragment {
	private ToggleButton visibilityBtn;
	private Spinner distanceBtn;
	private ToggleButton saveIdsBtn;
	private Button mapTypeBtn;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.personal_page, container, false);

		this.visibilityBtn = (ToggleButton) rootView.findViewById(R.id.visibilityBtn);
		this.visibilityBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Is the toggle on?
				boolean on = ((ToggleButton) v).isChecked();

				if (on) {
					GPSTracker gps = new GPSTracker(getSherlockActivity());
					if (gps.canGetLocation()) {
						double latitude = gps.getLatitude();
						double longitude = gps.getLongitude();

						new UpdateLocation(getSherlockActivity(), longitude, latitude).execute();
					}
				} else {
					new ResetLocation(getSherlockActivity()).execute();
				}
			}
		});

		this.distanceBtn = (Spinner) rootView.findViewById(R.id.distanceBtn);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getSherlockActivity(), R.array.distance_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.distanceBtn.setAdapter(adapter);
		this.distanceBtn.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				MapTab.radius = Double.parseDouble((String) parent.getItemAtPosition(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		this.saveIdsBtn = (ToggleButton) rootView.findViewById(R.id.saveIdsBtn);
		this.saveIdsBtn.setChecked(LocalStorage.getBooleanData(getSherlockActivity(), Keys.SAVING_STATE));
		this.saveIdsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Is the toggle on?
				boolean on = ((ToggleButton) v).isChecked();

				if (on) {
					LocalStorage.saveStringData(getSherlockActivity(), Keys.EMAIL, LoginPage.getEmailValue());
					LocalStorage.saveStringData(getSherlockActivity(), Keys.PASSWORD, LoginPage.getPasswordValue());
					LocalStorage.saveBooleanData(getSherlockActivity(), Keys.SAVING_STATE, true);
				} else {
					LocalStorage.removeStringData(getSherlockActivity(), Keys.EMAIL, Keys.PASSWORD);
					LocalStorage.saveBooleanData(getSherlockActivity(), Keys.SAVING_STATE, false);
				}
			}
		});

		this.mapTypeBtn = (Button) rootView.findViewById(R.id.mapTypeBtn);
		this.mapTypeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Strings to Show In Dialog with Radio Buttons
				final String[] items = getResources().getStringArray(R.array.mapTypes);

				// Creating and Building the Dialog
				AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
				builder.setTitle(getString(R.string.mapTypes));
				int checkedItem = 0;
				if (!(LocalStorage.getIntData(getSherlockActivity(), Keys.SAVING_MAP_STATE) == -1)) {
					checkedItem = LocalStorage.getIntData(getSherlockActivity(), Keys.SAVING_MAP_STATE) - 1;
				}
				builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						MapTab.mapType = item + 1;
						LocalStorage.saveIntData(getSherlockActivity(), Keys.SAVING_MAP_STATE, MapTab.mapType);
						dialog.cancel();
					}
				});
				// Create alert dialog
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		});

		return rootView;
	}

	private class ResetLocation extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;

		public ResetLocation(Context context) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
		}

		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.LONGITUDE, Keys.DEFAULT_LONGITUDE));
			params.add(new BasicNameValuePair(Keys.LATITUDE, Keys.DEFAULT_LATITUDE));

			// getting JSON Object
			String jsonObj = new JSONParser().makeHttpRequest(Keys.UPDATE_LOCATION_URL, Keys.POST_METHOD, params);
			return jsonObj;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	private class UpdateLocation extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;

		private Double longtitude;
		private Double latitude;

		public UpdateLocation(Context context, Double longtitude, Double latitude) {
			this.longtitude = longtitude;
			this.latitude = latitude;
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
		}

		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.LONGITUDE, Double.toString(this.longtitude)));
			params.add(new BasicNameValuePair(Keys.LATITUDE, Double.toString(this.latitude)));

			// getting JSON Object
			String jsonObj = new JSONParser().makeHttpRequest(Keys.UPDATE_LOCATION_URL, Keys.POST_METHOD, params);
			return jsonObj;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		menu.clear();

		MenuItem panier = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, getString(R.string.searchTitle));
		{
			panier.setIcon(R.drawable.ic_action_search);
			panier.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			ExceptionHandler.getInstance().mHandler(e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.getInstance().mHandler(e);
			throw new RuntimeException(e);
		}
	}
}

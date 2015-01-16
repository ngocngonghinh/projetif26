package utt.fr.if26.project.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import utt.fr.if26.project.R;
import utt.fr.if26.project.pages.adapters.MenuListAdapter;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import utt.fr.if26.project.support.LocalStorage;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

@SuppressWarnings("deprecation")
public class MainActivity extends SherlockFragmentActivity {
	// Declare variables
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private MenuListAdapter mMenuAdapter;
	private String[] title;
	private String[] subtitle;
	private int[] icon;
	private Fragment tabsPage = new TabsPage();
	private Fragment personalPage = new PersonalPage();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_list);

		// Generate title
		title = new String[] { getString(R.string.tabsPageTitle), getString(R.string.personalPageTitle), getString(R.string.logoutTitle) };

		// Generate subtitle
		subtitle = new String[] { getString(R.string.tabsPageSub), getString(R.string.personalPageSub), getString(R.string.logoutSub) };

		// Generate icon
		icon = new int[] { R.drawable.ic_action_go_to_today, R.drawable.ic_action_person, R.drawable.ic_action_cancel };

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// Pass results to MenuListAdapter Class
		mMenuAdapter = new MenuListAdapter(this, title, subtitle, icon);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		// Capture button clicks on side menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);

		menu.clear();

		MenuItem panier = menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, getString(R.string.searchTitle));
		{
			panier.setIcon(R.drawable.ic_action_search);
			panier.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			return super.onOptionsItemSelected(item);

		case Menu.FIRST + 1:
			Intent i = new Intent(this, FriendsSearch.class);
			startActivity(i);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
		// Locate Position
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, tabsPage);
			break;
		case 1:
			ft.replace(R.id.content_frame, personalPage);
			break;
		case 2:
			new ResetLocation().execute();
			LoginPage.setToken("");
			LocalStorage.removeStringData(this, Keys.EMAIL, Keys.PASSWORD);
			Intent i = new Intent(this, LoginPage.class);
			startActivity(i);
			break;
		}
		ft.commit();
		mDrawerList.setItemChecked(position, true);
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class ResetLocation extends AsyncTask<String, Void, String> {
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
	}

	public MainActivity getMainAtivity() {
		return this;
	}
}

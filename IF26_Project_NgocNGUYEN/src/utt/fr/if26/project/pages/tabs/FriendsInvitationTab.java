package utt.fr.if26.project.pages.tabs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.helpers.ViewHelper;
import utt.fr.if26.project.model.User;
import utt.fr.if26.project.pages.LoginPage;
import utt.fr.if26.project.pages.adapters.FriendsListAdapter;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class FriendsInvitationTab extends SherlockFragment {
	private ListView friendsListView;
	private FriendsListAdapter friendsListAdapter;

	public static FriendsInvitationTab newIntance() {
		FriendsInvitationTab frag = new FriendsInvitationTab();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.friends_tab, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.friendsListView = (ListView) view.findViewById(R.id.custom_list);

		new GetInvitingFriends(Keys.INVITATIONS_URL).execute();

	}

	private class GetInvitingFriends extends AsyncTask<String, Void, String> {
		private String url;

		public GetInvitingFriends(String url) {
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ViewHelper.switchViewSwitcher(FriendsInvitationTab.this, 0);
		}

		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			// getting JSONObject result
			String result = new JSONParser().makeHttpRequest(this.url, Keys.GET, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			ViewHelper.switchViewSwitcher(FriendsInvitationTab.this, 1);

			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray(Keys.FRIENDS);

				ArrayList<User> list = new ArrayList<User>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					User user = new User(obj);
					user.setStatus(Keys.INVITING_FRIEND);
					list.add(user);
				}

				createUI(list);
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	public void createUI(ArrayList<User> list) {
		this.friendsListAdapter = new FriendsListAdapter(getSherlockActivity(), list, 1);
		this.friendsListView.setAdapter(friendsListAdapter);
		this.friendsListView.setDivider(new ColorDrawable(getResources().getColor(R.color.blue)));
		this.friendsListView.setDividerHeight(5);
	}
}

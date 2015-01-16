package utt.fr.if26.project.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.User;
import utt.fr.if26.project.pages.adapters.FriendsListAdapter;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FriendsPage extends SherlockFragmentActivity {
	private ListView friendsListView;
	private FriendsListAdapter friendsListAdapter;

	private ArrayList<User> allFriends;
	private ArrayList<User> invitingFriends;
	private ArrayList<User> requestedFriends;
	private ArrayList<User> alreadyFriends;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.friends_list);
		this.friendsListView = (ListView) this.findViewById(R.id.custom_list);

		allFriends = (ArrayList<User>) getIntent().getExtras().getSerializable(Keys.FRIENDS_LIST);

		new GetInvitingFriends(FriendsPage.this, Keys.INVITATIONS_URL).execute();
	}

	private class GetInvitingFriends extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;

		public GetInvitingFriends(Context context, String url) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.url = url;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			String result = new JSONParser().makeHttpRequest(this.url, Keys.POST_METHOD, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray(Keys.FRIENDS);

				invitingFriends = new ArrayList<User>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					User user = new User(obj);
					invitingFriends.add(user);
				}

				new GetRequestedFriends(this.context, Keys.SEND_REQUEST_URL).execute();
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	private class GetRequestedFriends extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;

		public GetRequestedFriends(Context context, String url) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.url = url;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			String result = new JSONParser().makeHttpRequest(this.url, Keys.POST_METHOD, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray(Keys.FRIENDS);

				requestedFriends = new ArrayList<User>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					User user = new User(obj);
					requestedFriends.add(user);
				}

				new GetAlreadyFriends(this.context, Keys.ALREADY_FRIENDS_URL).execute();
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	private class GetAlreadyFriends extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;

		public GetAlreadyFriends(Context context, String url) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.url = url;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			String result = new JSONParser().makeHttpRequest(this.url, Keys.POST_METHOD, params);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONArray jsonArray = jsonObject.getJSONArray(Keys.FRIENDS);

				alreadyFriends = new ArrayList<User>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					User user = new User(obj);
					alreadyFriends.add(user);
				}

				int nbTypes = 0;
				for (User user : allFriends) {
					String id = user.getId();
					for (User invitingFriend : invitingFriends) {
						if (invitingFriend.getId().equals(id)) {
							user.setStatus(Keys.INVITING_FRIEND);
						}
					}

					for (User requestedFriend : requestedFriends) {
						if (requestedFriend.getId().equals(id)) {
							user.setStatus(Keys.REQUESTED_FRIEND);
						}
					}

					for (User alreadyFriend : alreadyFriends) {
						if (alreadyFriend.getId().equals(id)) {
							user.setStatus(Keys.ALREADY_FRIEND);
						}
					}

					if (user.getStatus() == Keys.NOT_AFFECTED) {
						user.setStatus(Keys.NOT_YET_FRIEND);
						nbTypes = 1;
					}
				}

				if (invitingFriends.size() > 0) {
					nbTypes++;
				}
				if (requestedFriends.size() > 0) {
					nbTypes++;
				}
				if (alreadyFriends.size() > 0) {
					nbTypes++;
				}

				createUI(allFriends, nbTypes);
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	public void createUI(ArrayList<User> list, int nbTypes) {
		this.friendsListAdapter = new FriendsListAdapter(this, list, nbTypes);
		this.friendsListView.setAdapter(friendsListAdapter);
		this.friendsListView.setDivider(new ColorDrawable(getResources().getColor(R.color.blue)));
		this.friendsListView.setDividerHeight(5);
	}
}
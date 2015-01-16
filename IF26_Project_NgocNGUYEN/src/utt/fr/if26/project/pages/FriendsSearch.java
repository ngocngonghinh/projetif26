package utt.fr.if26.project.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.Contact;
import utt.fr.if26.project.model.User;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FriendsSearch extends SherlockFragmentActivity {
	private ListView friendsListView;
	private SimpleAdapter listAdapter;
	private ArrayList<User> alreadyFriends;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		setContentView(R.layout.friends_search_page);
		new GetAlreadyFriends(this, Keys.ALREADY_FRIENDS_URL).execute();
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

				createUI(alreadyFriends);
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	public void createUI(final ArrayList<User> list) {
		friendsListView = (ListView) findViewById(R.id.listNearFriends);
		ArrayList<HashMap<String, String>> listFriends = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < list.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Keys.IMG, String.valueOf(R.drawable.avatar));
			map.put(Keys.NAME, ((User) list.get(i)).getName());
			map.put(Keys.ID, ((User) list.get(i)).getId());
			listFriends.add(map);
		}

		listAdapter = new SimpleAdapter(this.getBaseContext(), listFriends, R.layout.simplerowfriend, new String[] { Keys.IMG, Keys.NAME }, new int[] { R.id.img, R.id.name });
		// Set the ArrayAdapter as the ListView's adapter.
		friendsListView.setAdapter(listAdapter);
		friendsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				User user = list.get(position);

				new GetContacts(Keys.GET_CONTACTS_URL, getFriendsSearchActivity(), user).execute();
			}
		});
		EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text
				listAdapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	public FriendsSearch getFriendsSearchActivity() {
		return this;
	}

	private class GetMessages extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;
		private Contact contact;

		public GetMessages(Context context, String url, Contact contact) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.url = url;
			this.contact = contact;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.CONTACT, Integer.toString(this.contact.getId())));
			JSONParser jsonParser = new JSONParser();
			String jsonObj = jsonParser.makeHttpRequest(this.url, Keys.POST_METHOD, params);

			return jsonObj;
		}

		@Override
		protected void onPostExecute(String result) {
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			Intent intent = new Intent(getFriendsSearchActivity(), MessagesPage.class);
			intent.putExtra(Keys.MESSAGE, result);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Keys.CONTACT, this.contact);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	private class GetContacts extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;
		private ArrayList<Contact> contactsList;
		private User user;

		public GetContacts(String url, Context context, User user) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.url = url;
			this.contactsList = new ArrayList<Contact>();
			this.user = user;
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

			contactsList = new JSONParser(result, JSONParser.JSON_CONTACTS).getlistContacts();

			boolean already = false;
			Object con = null;
			for (Contact contact : contactsList) {
				if (user.getName().equals(contact.getName())) {
					already = true;
					con = contact;
				}
			}

			if (already) {
				new GetMessages(getFriendsSearchActivity(), Keys.GET_MESSAGES_URL, (Contact) con).execute();
			} else {
				new CreateContact(Keys.CREATE_CONTACT, context, user).execute();
			}
		}
	}

	private class CreateContact extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String url;
		private User user;

		public CreateContact(String url, Context context, User user) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.url = url;
			this.user = user;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.ID, user.getId()));
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
				JSONObject contact = jsonObject.getJSONObject(Keys.CONTACT);

				Contact contact2 = new Contact(Integer.parseInt(contact.get(Keys.ID).toString()), user.getName());

				Intent intent = new Intent(getFriendsSearchActivity(), MessagesPage.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Keys.CONTACT, contact2);
				intent.putExtras(bundle);

				startActivity(intent);
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

}

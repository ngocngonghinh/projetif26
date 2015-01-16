package utt.fr.if26.project.pages.tabs;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import utt.fr.if26.project.R;
import utt.fr.if26.project.helpers.ViewHelper;
import utt.fr.if26.project.model.Contact;
import utt.fr.if26.project.pages.LoginPage;
import utt.fr.if26.project.pages.MessagesPage;
import utt.fr.if26.project.pages.adapters.ContactsListAdapter;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class ContactsTab extends SherlockFragment {
	private ListView contactsListView;
	private ContactsListAdapter contactsListAdapter;
	private ArrayList<Contact> contactsList;

	public static ArrayList<Contact> contacts;

	public static ContactsTab newIntance() {
		ContactsTab frag = new ContactsTab();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.contacts_tab, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.contactsListView = (ListView) view.findViewById(R.id.custom_list);

		new GetContacts(Keys.GET_CONTACTS_URL).execute();
	}

	private class GetContacts extends AsyncTask<String, Void, String> {
		private String url;

		public GetContacts(String url) {
			this.url = url;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ViewHelper.switchViewSwitcher(ContactsTab.this, 0);
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
			ViewHelper.switchViewSwitcher(ContactsTab.this, 1);

			contactsList = new JSONParser(result, JSONParser.JSON_CONTACTS).getlistContacts();

			createUI(contactsList);
		}
	}

	public void createUI(ArrayList<Contact> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getMessage() == null) {
				list.remove(i);
			}
		}

		this.contactsListAdapter = new ContactsListAdapter(getSherlockActivity(), list);
		this.contactsListView.setAdapter(contactsListAdapter);
		this.contactsListView.setDivider(new ColorDrawable(getResources().getColor(R.color.blue)));
		this.contactsListView.setDividerHeight(5);

		this.contactsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Contact contact = (Contact) contactsListView.getItemAtPosition(position);
				new GetMessages(getSherlockActivity(), Keys.GET_MESSAGES_URL, contact).execute();
			}
		});
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

			Intent intent = new Intent(getSherlockActivity(), MessagesPage.class);
			intent.putExtra(Keys.MESSAGE, result);
			Bundle bundle = new Bundle();
			bundle.putSerializable(Keys.CONTACT, this.contact);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		new GetContacts(Keys.GET_CONTACTS_URL).execute();
	}
}

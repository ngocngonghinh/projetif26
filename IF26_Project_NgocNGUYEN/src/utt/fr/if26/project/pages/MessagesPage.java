package utt.fr.if26.project.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.model.Contact;
import utt.fr.if26.project.model.Message;
import utt.fr.if26.project.pages.adapters.MessageAdapter;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.InputValidation;
import utt.fr.if26.project.support.JSONParser;
import utt.fr.if26.project.support.Keys;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class MessagesPage extends SherlockFragmentActivity {
	private ArrayList<Message> messagesList;
	private ListView msgListView;
	private MessageAdapter msgListAdapter;
	private Contact contact;
	private String message;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.messages_page);

		this.message = getIntent().getStringExtra(Keys.MESSAGE);
		this.contact = (Contact) getIntent().getExtras().getSerializable(Keys.CONTACT);

		this.setTitle(this.contact.getName());
		this.msgListView = (ListView) findViewById(R.id.listMessages);
		this.messagesList = new ArrayList<Message>();
		this.msgListAdapter = new MessageAdapter(this, null);
		this.msgListView.setAdapter(msgListAdapter);

		if (this.message != null) {
			this.messagesList = new JSONParser(this.message, JSONParser.JSON_MESSAGERS).getMessagesList();
		} else {
			this.messagesList = new ArrayList<Message>();
		}

		updateListView();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void sent(View view) {
		EditText mes = (EditText) findViewById(R.id.message);
		String message;
		message = InputValidation.cleanString(mes.getText().toString());
		new SendMessage(message).execute();
		mes.setText(Keys.EMPTY_MESSAGE);
	}

	public class SendMessage extends AsyncTask<String, Void, String> {
		String message;

		public SendMessage(String message) {
			this.message = message;
		}

		@Override
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.TOKEN, LoginPage.getToken()));
			params.add(new BasicNameValuePair(Keys.CONTACT, Integer.toString(contact.getId())));
			params.add(new BasicNameValuePair(Keys.MESSAGE, message));
			JSONParser jsonParser = new JSONParser();
			String json = jsonParser.makeHttpRequest(Keys.GET_MESSAGE_URL, Keys.POST_METHOD, params);

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			JSONObject jObj2;
			try {
				JSONObject jObj = new JSONObject(result);
				jObj2 = jObj.getJSONObject(Keys.MESSAGE);
				Message mes = new Message(jObj2);
				addMessage(mes);
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	public void addMessage(Message mes) {
		this.messagesList.add(mes);
		this.msgListAdapter = new MessageAdapter(getApplicationContext(), null);
		updateListView();
	}

	public void updateListView() {
		this.msgListAdapter.setData(this.messagesList);
		if (this.messagesList != null && this.messagesList.size() > 0) {
			this.msgListView.setSelection(this.messagesList.size() - 1);
		}
	}
}

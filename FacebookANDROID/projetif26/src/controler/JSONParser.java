package controler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import model.Contact;
import model.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * JSONParser is a class to get json from url and parse it
 * 
 * @author NNN
 *
 */
public class JSONParser {
	public static final int JSON_TOKEN = 0;
	public static final int JSON_CONTACTS = 1;
	public static final int JSON_MESSAGERS = 2;

	private InputStream is = null;
	private JSONObject jObj = null;
	private String json = "";
	private int typeJSON;

	public JSONParser() {
	}

	public JSONParser(String json, int type) {
		this.json = json;
		this.typeJSON = type;
	}

	// Get json from url by making HTTP POST or GET request
	public String makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request, to define InputStream is
		try {
			// Check request method type
			if (method == "POST") {
				// Request method is POST
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else if (method == "GET") {
				// Request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8")
						.replace("%40", "@");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "makeHttpRequest");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "makeHttpRequest");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "makeHttpRequest");
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Exception",
					"makeHttpRequest, Buffer Error converting result "
							+ e.getMessage());
			e.printStackTrace();
		}

		return json;
	}

	public JSONObject convertStringtoJSON(String json) {
		// Convert the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSONException",
					"makeHttpRequest, Error parsing data " + e.getMessage());
			e.printStackTrace();
		}
		return jObj;
	}

	public ArrayList<Contact> getlistContacts() {
		ArrayList<Contact> list = null;
		if (this.typeJSON == JSON_CONTACTS) {
			// List consist of contact's information
			list = new ArrayList<Contact>();

			try {
				JSONObject jObj = new JSONObject(json);
				JSONArray jArr;
				try {
					jArr = jObj.getJSONArray("contacts");
					for (int i = 0; i < jArr.length(); i++) {
						JSONObject obj = jArr.getJSONObject(i);
						Contact con = new Contact(obj);
						list.add(con);
					}
				} catch (JSONException e) {
					Log.e("JSONException",
							"getlistContacts, JSONArray not parseable");
					e.printStackTrace();
				}

			} catch (JSONException e) {
				Log.e("JSONException",
						"getlistContacts, JSONObject not parseable");
				e.printStackTrace();
			}

			return list;
		}

		return list;
	}

	public ArrayList<Message> getMessagesList() {
		// List consist of message's information
		ArrayList<Message> list = null;
		if (this.typeJSON == JSON_MESSAGERS) {
			list = new ArrayList<Message>();
			try {
				JSONObject jObj = new JSONObject(json);
				try {
					JSONArray jArr = jObj.getJSONArray("messages");
					for (int i = 0; i < jArr.length(); i++) {
						JSONObject obj = jArr.getJSONObject(i);
						Message message = new Message(obj);
						list.add(message);
					}
				} catch (JSONException e) {
					Log.e("JSONException",
							"getListMessage(), JSONArray not parseable");
					e.printStackTrace();
				}
			} catch (JSONException e) {
				Log.e("JSONException",
						"getListMessage(), JSONObject not parseable");
				e.printStackTrace();
			}

			return list;
		}

		return list;
	}
}

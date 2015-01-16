package utt.fr.if26.project.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import utt.fr.if26.project.model.Contact;
import utt.fr.if26.project.model.Message;

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

	/**
	 * Get json from url by making HTTP POST or GET request
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 */
	public String makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request, to define what InputStream is
		try {
			// Check request method type
			if (method.equals(Keys.POST_METHOD)) {
				// Request method is POST
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else if (method.equals(Keys.GET_METHOD)) {
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
			ExceptionHandler.getInstance().mHandler(e);
		} catch (ClientProtocolException e) {
			ExceptionHandler.getInstance().mHandler(e);
		} catch (IOException e) {
			ExceptionHandler.getInstance().mHandler(e);
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
			ExceptionHandler.getInstance().mHandler(e);
		}

		return json;
	}

	/**
	 * Convert the string to a JSON object
	 * 
	 * @param json
	 * @return
	 */
	public JSONObject convertStringtoJSON(String json) {
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			ExceptionHandler.getInstance().mHandler(e);
		}
		return jObj;
	}

	/**
	 * List consist of contact's information
	 * 
	 * @return
	 */
	public ArrayList<Contact> getlistContacts() {
		ArrayList<Contact> list = null;
		if (this.typeJSON == JSON_CONTACTS) {
			list = new ArrayList<Contact>();

			try {
				JSONObject jObj = new JSONObject(json);
				JSONArray jArr;
				try {
					jArr = jObj.getJSONArray(Keys.CONTACTS);
					for (int i = 0; i < jArr.length(); i++) {
						JSONObject obj = jArr.getJSONObject(i);
						Contact con = new Contact(obj);
						list.add(con);
					}
				} catch (JSONException e) {
					ExceptionHandler.getInstance().mHandler(e);
				}
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}

			return list;
		}

		return list;
	}

	/**
	 * List consist of message's information
	 * 
	 * @return
	 */
	public ArrayList<Message> getMessagesList() {
		ArrayList<Message> list = null;
		if (this.typeJSON == JSON_MESSAGERS) {
			list = new ArrayList<Message>();
			try {
				JSONObject jObj = new JSONObject(json);
				try {
					JSONArray jArr = jObj.getJSONArray(Keys.MESSAGES);
					for (int i = 0; i < jArr.length(); i++) {
						JSONObject obj = jArr.getJSONObject(i);
						Message message = new Message(obj);
						list.add(message);
					}
				} catch (JSONException e) {
					ExceptionHandler.getInstance().mHandler(e);
				}
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}

			return list;
		}

		return list;
	}
}

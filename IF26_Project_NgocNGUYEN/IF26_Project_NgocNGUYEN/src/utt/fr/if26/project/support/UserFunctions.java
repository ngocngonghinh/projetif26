package utt.fr.if26.project.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import android.content.Context;

public class UserFunctions {

	private JSONParser jsonParser;
	private Context context;

	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	public UserFunctions(Context context) {
		jsonParser = new JSONParser();
		this.context = context;
	}

	/**
	 * Function making Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Keys.EMAIL, email));
		params.add(new BasicNameValuePair(Keys.PASSWORD, password));
		String jsonStr = jsonParser.makeHttpRequest(Keys.LOGIN_URL, Keys.GET, params);
		JSONObject jsonObj = jsonParser.convertStringtoJSON(jsonStr);
		return jsonObj;
	}

	/**
	 * Function making Registration Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password) {
		JSONObject jsonObj;
		if (!PasswordStrength.checkPasswordStrength(password)) {
			jsonObj = new JSONObject();
			try {
				jsonObj.put(Keys.ERROR, true);
				jsonObj.put(Keys.INDICATOR, this.context.getString(R.string.notStrongPassword));
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		} else {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Keys.NAME, name));
			params.add(new BasicNameValuePair(Keys.EMAIL, email));
			params.add(new BasicNameValuePair(Keys.PASSWORD, password));

			String jsonStr = jsonParser.makeHttpRequest(Keys.REGISTRATION_URL, Keys.GET, params);
			jsonObj = jsonParser.convertStringtoJSON(jsonStr);
		}

		return jsonObj;
	}
}
package controler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;

	private static String loginURL = "http://projetif26.byethost7.com/login.php";
	private static String registerURL = "http://projetif26.byethost7.com/createAccount.php";
	private static String KEY_PASSWORD = "password";
	private static String KEY_EMAIL = "email";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(KEY_EMAIL, email));
		params.add(new BasicNameValuePair(KEY_PASSWORD, password));
		String string_json = jsonParser
				.makeHttpRequest(loginURL, "GET", params);
		JSONObject json = jsonParser.convertStringtoJSON(string_json);
		// return json
		Log.i("JSON", json.toString());
		return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		String string_json = jsonParser.makeHttpRequest(registerURL, "GET",
				params);
		JSONObject json = jsonParser.convertStringtoJSON(string_json);
		Log.i("JSON", json.toString());
		// return json
		return json;
	}

	// /**
	// * Function get Login status
	// * */
	// public boolean isUserLoggedIn(Context context) {
	// DatabaseHandler db = new DatabaseHandler(context);
	// int count = db.getRowCount();
	// if (count > 0) {
	// // user logged in
	// return true;
	// }
	// return false;
	// }

	// /**
	// * Function to logout user Reset Database
	// * */
	// public boolean logoutUser(Context context) {
	// DatabaseHandler db = new DatabaseHandler(context);
	// db.resetTables();
	// return true;
	// }

}
package info.androidhive.info;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import controler.InputValidation;
import controler.UserFunctions;

public class LoginActivity extends Activity {
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;
	private ProgressDialog pDialog;
	// JSON Response node names
	private static String KEY_ERROR = "error";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);
	}

	// Login button Click Event
	public void login(View view) {
		String email = inputEmail.getText().toString();
		String password = inputPassword.getText().toString();
		// Prevent SQL injection attacks
		if (InputValidation.isEmail(email)) {
			if (isConnected())
			new GetToken(email, password).execute();
		} else
			Toast.makeText(getApplicationContext(), R.string.errorInput,
					Toast.LENGTH_SHORT).show();
	}
	// Check network connection
	@SuppressWarnings("static-access")
	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this
				.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), R.string.errorInternet,
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	public class GetToken extends AsyncTask<String, Void, String> {
		String email;
		String password;

		public GetToken(String email, String password) {
			this.email = email;
			this.password = password;
		}

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LoginActivity.this);
			pDialog.setMessage(getResources().getString(R.string.loading));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(email, password);
			return json.toString();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// try {
			// JSONObject jObj = new JSONObject(result);
			// if (jObj.getBoolean("error") == true) {
			// Toast.makeText(getApplicationContext(),
			// R.string.errorLogin, Toast.LENGTH_SHORT).show();
			// } else {
			// token = jObj.getString("token");
			// new GetContacts(token).execute();
			// }
			// } catch (JSONException e) {
			// Log.e("JSONException",
			// "onPostExecute, GetToken, " + e.getMessage());
			// e.printStackTrace();
			// }
			try {
				JSONObject json = new JSONObject(result);
				if (json.getBoolean(KEY_ERROR) != true) {
					// user successfully logged in
					// Launch Dashboard Screen
					Intent map = new Intent(getApplicationContext(),
							DashboardActivity.class);

					// Close all views before launching Map
					//map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(map);
					// Close Login Screen
					finish();
				} else {
					// Error in login
					loginErrorMsg.setText("Incorrect username/password");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Close the dialog once done
			pDialog.dismiss();
		}

		// Link to Register Screen

		public void register(View view) {
			Intent i = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(i);
			finish();
		}
	}
}

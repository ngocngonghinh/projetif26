package utt.fr.if26.project.pages;

import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.helpers.PrefsHelper;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.InputValidation;
import utt.fr.if26.project.support.Keys;
import utt.fr.if26.project.support.LocalStorage;
import utt.fr.if26.project.support.UserFunctions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoginPage extends SherlockFragmentActivity {
	private EditText inputEmail;
	private EditText inputPassword;
	private TextView loginErrorMsg;
	private static String token;
	private static String emailValue;
	private static String passwordValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.login_page);
		this.inputEmail = (EditText) findViewById(R.id.loginEmail);
		this.inputPassword = (EditText) findViewById(R.id.loginPassword);
		this.loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// If the ids was saved locally, the user don't need to be authenticated
		if (!LocalStorage.getStringData(this, Keys.EMAIL).equals("") && !LocalStorage.getStringData(this, Keys.PASSWORD).equals("")) {
			new GetToken(this, LocalStorage.getStringData(this, Keys.EMAIL), LocalStorage.getStringData(this, Keys.PASSWORD)).execute();
		}
	}

	/**
	 * Login button click event
	 * 
	 * @param view
	 */
	public void login(View view) {
		String email = this.inputEmail.getText().toString();
		String password = this.inputPassword.getText().toString();
		// Prevent SQL injection attacks
		if (InputValidation.isEmail(email)) {
			if (isNetworkConnected()) {
				new GetToken(this, email, password).execute();
			}
		} else {
			// If email is not correct, risk of SQL injection
			Toast.makeText(getApplicationContext(), R.string.errorInput, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Check network connection
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean isNetworkConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), R.string.errorInternet, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		LoginPage.token = token;
	}

	public static String getEmailValue() {
		return emailValue;
	}

	public static String getPasswordValue() {
		return passwordValue;
	}

	/**
	 * Authentication process
	 */
	private class GetToken extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String email;
		private String password;

		public GetToken(Context context, String email, String password) {
			this.context = context;
			// Before starting background thread Show Progress Dialog
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.email = email;
			this.password = password;
		}

		@Override
		protected String doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(email, password);
			return json.toString();
		}

		// Displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// Close the dialog once done
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
			try {
				JSONObject json = new JSONObject(result);
				if (json.getBoolean(Keys.ERROR) != true) {
					// User successfully logged in
					LoginPage.token = json.getString(Keys.TOKEN);
					LoginPage.emailValue = this.email;
					LoginPage.passwordValue = this.password;
					if (!TextUtils.isEmpty(token)) {
						PrefsHelper.setIsLogged(LoginPage.this, true);
						PrefsHelper.setString(LoginPage.this, token);
					}
					Intent map = new Intent(LoginPage.this, MainActivity.class);

					// Close all views before launching Map
					startActivity(map);
					// Close login screen
					finish();
				} else {
					if (json.has(Keys.REFUSED_ERROR)) {
						loginErrorMsg.setText(json.getString(Keys.REFUSED_ERROR));
					} else {
						// Error in login
						loginErrorMsg.setText(getString(R.string.errorLoginMsg));
					}
				}
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}

	/**
	 * Link to registration page
	 * 
	 * @param view
	 */
	public void register(View view) {
		Intent i = new Intent(this, RegisterPage.class);
		startActivity(i);
		finish();
	}
}

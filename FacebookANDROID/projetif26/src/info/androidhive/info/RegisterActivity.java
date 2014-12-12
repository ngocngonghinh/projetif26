package info.androidhive.info;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import controler.InputValidation;
import controler.UserFunctions;

public class RegisterActivity extends Activity {
	Button btnRegister;
	Button btnLinkToLogin;
	EditText inputFullName;
	EditText inputEmail;
	EditText inputPassword;
	TextView registerErrorMsg;
	private ProgressDialog pDialog;

	// JSON Response node names
	private static String KEY_ERROR = "error";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				// Prevent SQL injection attacks
				if (InputValidation.isEmail(email)) {
					new GetToken(name,email, password).execute();
				} else
					Toast.makeText(getApplicationContext(), R.string.errorInput,
							Toast.LENGTH_SHORT).show();

			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				// Close Registration View
				finish();
			}
		});
	}

	public class GetToken extends AsyncTask<String, Void, String> {
		String email;
		String password;
		String name;

		public GetToken(String name,String email, String password) {
			this.name=name;
			this.email = email;
			this.password = password;
		}

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage(getResources().getString(R.string.loading));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.registerUser(name,email, password);
			return json.toString();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject json = new JSONObject(result);
				// check for login response
				if (json.getBoolean(KEY_ERROR) != true) {
					registerErrorMsg.setText("");
					// Launch Map Screen
					Intent map = new Intent(getApplicationContext(),
							DashboardActivity.class);
					// Close all views before launching Map
					map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(map);
					// Close Registration Screen
					finish();
				} else {
					// Error in registration
					registerErrorMsg.setText("Error occured in registration");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Close the dialog once done
			pDialog.dismiss();
		}
	}
}

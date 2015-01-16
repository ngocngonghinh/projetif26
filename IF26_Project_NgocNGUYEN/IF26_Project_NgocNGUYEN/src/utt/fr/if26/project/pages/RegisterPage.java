package utt.fr.if26.project.pages;

import org.json.JSONException;
import org.json.JSONObject;

import utt.fr.if26.project.R;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.InputValidation;
import utt.fr.if26.project.support.Keys;
import utt.fr.if26.project.support.UserFunctions;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterPage extends Activity {
	private Button registerBtn;
	private Button linkToLoginBtn;
	private EditText inputFullName;
	private EditText inputEmail;
	private EditText inputPassword;
	private TextView registerErrorMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_page);

		// Importing all assets like buttons, text fields
		this.inputFullName = (EditText) findViewById(R.id.registerName);
		this.inputEmail = (EditText) findViewById(R.id.registerEmail);
		this.inputPassword = (EditText) findViewById(R.id.registerPassword);
		this.registerBtn = (Button) findViewById(R.id.btnRegister);
		this.linkToLoginBtn = (Button) findViewById(R.id.btnLinkToLoginScreen);
		this.registerErrorMsg = (TextView) findViewById(R.id.register_error);

		// Register Button Click event
		this.registerBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				// Prevent SQL injection attacks
				if (InputValidation.isEmail(email)) {
					new GetToken(getApplicationContext(), name, email, password).execute();
				} else
					Toast.makeText(getApplicationContext(), R.string.errorInput, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private class GetToken extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context context;
		private String email;
		private String password;
		private String name;

		// Before starting background thread Show Progress Dialog
		public GetToken(Context context, String name, String email, String password) {
			this.context = context;
			this.dialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.loading), true, true, null);
			this.name = name;
			this.email = email;
			this.password = password;
		}

		@Override
		protected String doInBackground(String... args) {
			UserFunctions userFunction = new UserFunctions(getApplicationContext());
			JSONObject json = userFunction.registerUser(name, email, password);
			return json.toString();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			// Close the dialog once done
			if (null != this.dialog && this.dialog.isShowing()) {
				this.dialog.dismiss();
			}

			try {
				JSONObject json = new JSONObject(result);
				// Check for login response
				if (json.getBoolean(Keys.ERROR) != true) {
					registerErrorMsg.setText("");
					// Launch Map Screen
					LoginPage.setToken(json.getString(Keys.TOKEN));

					// Link to Login Screen
					linkToLoginBtn.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent i = new Intent(getApplicationContext(), LoginPage.class);
							startActivity(i);
							// Close Registration View
							finish();
						}
					});

					Intent map = new Intent(getApplicationContext(), MainActivity.class);
					// Close all views before launching Map
					map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(map);
					// Close Registration Screen
					finish();
				} else {
					if (json.has(Keys.INDICATOR)) {
						registerErrorMsg.setText(json.getString(Keys.INDICATOR));
					} else {
						// Error in registration
						registerErrorMsg.setText(R.string.errorRegisteration);
					}
				}
			} catch (JSONException e) {
				ExceptionHandler.getInstance().mHandler(e);
			}
		}
	}
}

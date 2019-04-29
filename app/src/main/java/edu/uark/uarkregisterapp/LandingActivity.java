package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Employee;
import edu.uark.uarkregisterapp.models.api.EmployeeSignIn;
import edu.uark.uarkregisterapp.models.api.services.EmployeeService;
import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class LandingActivity extends AppCompatActivity {
	private static final String TAG = "LandingActivity";
	private TextToSpeech textToSpeech;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);

		getSupportActionBar().hide();

		textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					int res = textToSpeech.setLanguage(Locale.ENGLISH);
					if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
						Log.i(TAG, "onInit: Language not supported");
					}
				} else {
					Log.i(TAG, "onInit: init not successful");
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();

		(new QueryActiveEmployeeExistsTask()).execute();
	}

	@Override
	protected void onDestroy() {
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		super.onDestroy();
	}

	public void speak(String message) {
			textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
	}
	public void signInButtonOnClick(View view) {
		if (StringUtils.isBlank(this.getEmployeeIdEditText().getText().toString())) {
			new AlertDialog.Builder(this)
				.setMessage(R.string.alert_dialog_employee_id_empty)
				.create()
				.show();
			this.getEmployeeIdEditText().requestFocus();

			return;
		}

		if (StringUtils.isBlank(this.getPasswordEditText().getText().toString())) {
			new AlertDialog.Builder(this)
				.setMessage(R.string.alert_dialog_employee_password_empty)
				.create()
				.show();
			this.getPasswordEditText().requestFocus();

			return;
		}

		(new SignInTask()).execute(
			(new EmployeeSignIn())
				.setEmployeeId(this.getEmployeeIdEditText().getText().toString())
				.setPassword(this.getPasswordEditText().getText().toString())
		);
	}

	private EditText getEmployeeIdEditText() {
		return (EditText) this.findViewById(R.id.edit_text_employee_id);
	}

	private EditText getPasswordEditText() {
		return (EditText) this.findViewById(R.id.edit_text_password);
	}

	private class QueryActiveEmployeeExistsTask extends AsyncTask<Void, Void, ApiResponse<Boolean>> {
		@Override
		protected ApiResponse<Boolean> doInBackground(Void... params) {
			return (new EmployeeService()).getActiveEmployeeExists();
		}

		@Override
		protected void onPostExecute(ApiResponse<Boolean> apiResponse) {
			if (apiResponse.isValidResponse() && apiResponse.getData()) {
				return;
			}

			new AlertDialog.Builder(LandingActivity.this)
				.setMessage(R.string.alert_dialog_no_employees_exist)
				.setPositiveButton(
					R.string.button_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(new Intent(getApplicationContext(), CreateEmployeeActivity.class));

							dialog.dismiss();
						}
					}
				)
				.create()
				.show();
		}
	}

	private class SignInTask extends AsyncTask<EmployeeSignIn, Void, ApiResponse<Employee>> {
		@Override
		protected void onPreExecute() {
			this.signInAlert = new AlertDialog.Builder(LandingActivity.this)
				.setMessage(R.string.alert_dialog_signing_in)
				.create();
			this.signInAlert.show();
		}

		@Override
		protected ApiResponse<Employee> doInBackground(EmployeeSignIn... employeeSignIns) {
			if (employeeSignIns.length > 0) {
				return (new EmployeeService()).signIn(employeeSignIns[0]);
			} else {
				return (new ApiResponse<Employee>())
					.setValidResponse(false);
			}
		}

		@Override
		protected void onPostExecute(ApiResponse<Employee> apiResponse) {
			this.signInAlert.dismiss();

			if (!apiResponse.isValidResponse()) {
				new AlertDialog.Builder(LandingActivity.this)
					.setMessage(R.string.alert_dialog_employee_sign_in_failed)
					.create()
					.show();
				return;
			}


			Intent intent = new Intent(getApplicationContext(), MainActivity.class);

			intent.putExtra(
				getString(R.string.intent_extra_employee)
				, new EmployeeTransition(apiResponse.getData())
			);

			speak("Welcome " + apiResponse.getData().getFirstName() + " to UARK Register App");
			startActivity(intent);
		}

		private AlertDialog signInAlert;
	}
}

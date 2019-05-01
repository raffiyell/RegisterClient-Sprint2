package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EmployeeTransition employeeTransition;
    private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));


        getSupportActionBar().hide();

        this.employeeTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_employee));

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int res = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "init unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void speak(String message) {

        if (message.isEmpty())
            Toast.makeText(MainActivity.this, "Enter message first", Toast.LENGTH_SHORT).show();
        else {
            textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            /// textToSpeech.setSpeechRate()
            Toast.makeText(MainActivity.this, "speaking", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        TextView employeeFirstName = findViewById(R.id.text_view_employee_welcome);
        employeeFirstName.setText(this.employeeTransition.getFirstName().substring(0, 1).toUpperCase() + this.employeeTransition.getFirstName().substring(1));
        TextView employeeIdTextView = findViewById(R.id.text_view_employee_id);
        employeeIdTextView.setText(this.employeeTransition.getEmployeeId());
    }

    public void beginTransactionButtonOnClick(View view) {

        Intent beginTransaction = new Intent(MainActivity.this, ProductsListingActivity.class);
        beginTransaction.putExtra("intent_employee_id", this.employeeTransition.getEmployeeId());
        beginTransaction.putExtra("intent_extra_employee", this.employeeTransition);

        speak("Please select items from the product list");
        startActivity(beginTransaction);
    }

    public void productSalesReportButtonOnClick(View view) {
        speak("report: product");

        Intent reportProduct = new Intent(MainActivity.this, ReportProductActivity.class);
        startActivity(reportProduct);
    }

    public void cashierSalesReportButtonOnClick(View view) {
        speak("report: cashier");
        Intent reportCashier = new Intent(MainActivity.this, ReportCashierActivity.class);
        startActivity(reportCashier);
    }

    public void createEmployeeButtonOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), CreateEmployeeActivity.class));
        speak("Please fill out the appropriate employee information");
    }

    public void logOutButtonOnClick(View view) {
        this.startActivity(new Intent(getApplicationContext(), LandingActivity.class));
        speak("Logging out");
    }

    private void displayFunctionalityNotAvailableDialog() {
        new AlertDialog.Builder(this).
                setMessage(R.string.alert_dialog_functionality_not_available).
                setPositiveButton(
                        R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }
                ).
                create().
                show();
    }

    //TODO finish this so the the employee name and id will be saved on the device. When the back button is pressed from the productslistingactivity is clicked, the name and id can be retrieved again

    private void saveSharedPreference() {

        SharedPreferences sharedPreferences = getSharedPreferences("Employee", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("employeeid", this.employeeTransition.getEmployeeId());
        editor.apply();

    }


    private void loadSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String loadedDateFormat = sharedPreferences.getString("DateFormat", "N/A");

    }


}

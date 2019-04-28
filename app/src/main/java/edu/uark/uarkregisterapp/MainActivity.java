package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import edu.uark.uarkregisterapp.models.transition.EmployeeTransition;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EmployeeTransition employeeTransition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        this.employeeTransition = this.getIntent().getParcelableExtra(this.getString(R.string.intent_extra_employee));

    }

    @Override
    protected void onStart() {
        super.onStart();

        this.getEmployeeWelcomeTextView().setText("Welcome " + this.employeeTransition.getFirstName() + " (" + this.employeeTransition.getEmployeeId() + ")!");
    }

    public void beginTransactionButtonOnClick(View view) {
        Intent beginTransaction = new Intent(MainActivity.this, ProductsListingActivity.class);
        beginTransaction.putExtra("intent_employee_id", this.employeeTransition.getEmployeeId());
        beginTransaction.putExtra("intent_extra_employee", this.employeeTransition);
        startActivity(beginTransaction);
    }

    public void productSalesReportButtonOnClick(View view) {
        this.displayFunctionalityNotAvailableDialog();
    }

    public void cashierSalesReportButtonOnClick(View view) {
        this.displayFunctionalityNotAvailableDialog();
    }

    public void createEmployeeButtonOnClick(View view) {
        startActivity(new Intent(getApplicationContext(), CreateEmployeeActivity.class));

    }

    public void logOutButtonOnClick(View view) {
        this.startActivity(new Intent(getApplicationContext(), LandingActivity.class));
    }

    private TextView getEmployeeWelcomeTextView() {
        return (TextView) this.findViewById(R.id.text_view_employee_welcome);
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

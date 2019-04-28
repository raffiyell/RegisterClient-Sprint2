package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class OrderComplete extends AppCompatActivity {

    private static final String TAG = "OrderComplete";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void returnToMain(View view){
        Log.i(TAG, "returnToMain: *********************" + this.getIntent().getParcelableExtra("intent_extra_employee").toString() );

        Intent returnToMain =new Intent(OrderComplete.this, MainActivity.class);
        returnToMain.putExtra(
                "intent_extra_employee", this.getIntent().getParcelableExtra("intent_extra_employee"));
        startActivity(returnToMain);

    }

}

package edu.uark.uarkregisterapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.uark.uarkregisterapp.adapters.ConfirmationListAdapter;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;

public class ConfirmationScreenActivity extends AppCompatActivity {
    private static final String TAG = "ConfirmationScreen";

    private ArrayList<TransactionEntryTransition> transactionEntryTransitionsCart;
    private ConfirmationListAdapter productListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        transactionEntryTransitionsCart = getIntent().getParcelableArrayListExtra("transactionEntryCart");

        //Toast.makeText(ConfirmationScreenActivity.this, productsInCart.toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(ConfirmationScreenActivity.this, transactionEntryTransitionsCart.toString(), Toast.LENGTH_SHORT).show();
        ListView confirmationListView = findViewById(R.id.confirmationListView);


        productListAdapter = new ConfirmationListAdapter(this, transactionEntryTransitionsCart);
        confirmationListView.setAdapter(productListAdapter);

        Log.i(TAG, "onCreate: s" + transactionEntryTransitionsCart.get(0).getQuantity() + "s *************************************************");

        TextView totalPriceTextView = findViewById(R.id.text_view_total_price_confirmation);
        getTotalPrice();

        totalPriceTextView.setText("Total price: " + getTotalPrice());
    }

    private double getTotalPrice(){
        double totPrice = 0.0;
        for (TransactionEntryTransition temp: transactionEntryTransitionsCart){
            totPrice += temp.getPrice() * temp.getQuantity();
        }
        return totPrice;
    }


}

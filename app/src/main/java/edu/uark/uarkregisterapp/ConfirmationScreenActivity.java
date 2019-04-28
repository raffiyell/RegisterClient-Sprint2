package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import edu.uark.uarkregisterapp.R.*;
import edu.uark.uarkregisterapp.adapters.ConfirmationListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.api.services.TransactionEntryService;
import edu.uark.uarkregisterapp.models.api.services.TransactionService;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionTransition;

public class ConfirmationScreenActivity extends AppCompatActivity {
    private static final String TAG = "ConfirmationScreen";

    private TransactionTransition transactionTransition;
    private ArrayList<TransactionEntryTransition> transactionEntryTransitionsCart;
    private ConfirmationListAdapter productListAdapter;
    private double totPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        transactionTransition = getIntent().getParcelableExtra("intent_extra_transition");
        transactionEntryTransitionsCart = getIntent().getParcelableArrayListExtra("transactionEntryCart");


        //Toast.makeText(ConfirmationScreenActivity.this, productsInCart.toString(), Toast.LENGTH_SHORT).show();
        //Toast.makeText(ConfirmationScreenActivity.this, transactionEntryTransitionsCart.toString(), Toast.LENGTH_SHORT).show();
        ListView confirmationListView = findViewById(R.id.confirmationListView);


        productListAdapter = new ConfirmationListAdapter(this, transactionEntryTransitionsCart);
        confirmationListView.setAdapter(productListAdapter);

        TextView totalPriceTextView = findViewById(R.id.text_view_total_price_confirmation);
        getTotalPrice();
        updateTransaction();
        totalPriceTextView.setText("Total price: $" + getTotalPrice());

        Log.i(TAG, "onCreate: " + transactionTransition.getRecordId() + " " + transactionTransition.getCashierId() + " " + transactionTransition.getCreatedOn() + " " + transactionTransition.getTotalAmount() + " " + transactionTransition.getTotalItemSold() + "***************************************");
        Log.i(TAG, "onCreate: s" + transactionEntryTransitionsCart.get(0).getQuantity() + "s *************************************************");
    }

    private double getTotalPrice() {
        totPrice = 0.0;
        for (TransactionEntryTransition temp : transactionEntryTransitionsCart) {
            totPrice += temp.getPrice() * temp.getQuantity();
        }
        return totPrice;
    }

    private void updateTransaction() {
        int totalItemSold = 0;

        for (TransactionEntryTransition temp : transactionEntryTransitionsCart) {
            totalItemSold += temp.getQuantity();
        }

        transactionTransition.setTotalAmount(totPrice);
        //transactionTransition.setTransactionType();
        transactionTransition.setTotalItemSold(totalItemSold);
    }

    public void confirmOrderButton(View view) {
        (new ConfirmTransactionTask()).execute();
    }

    private class ConfirmTransactionTask extends AsyncTask<Void, Void, Boolean> {
        AlertDialog processTransactionAlert;

        @Override
        protected void onPreExecute() {
            processTransactionAlert = new AlertDialog.Builder(ConfirmationScreenActivity.this).
                    setMessage("Processing Transaction...").
                    create();

            processTransactionAlert.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Transaction transaction = (new Transaction(transactionTransition));

            for (int i = 0; i < transactionEntryTransitionsCart.size(); i++) {
                TransactionEntry transactionEntry = (new TransactionEntry(ConfirmationScreenActivity.this.transactionEntryTransitionsCart.get(i)));
                transactionEntry.setTransactionReferenceId(transactionTransition.getRecordId());
                Log.i(TAG, "doInBackground: transaction Reference id" + transactionEntry.getTransactionReferenceId()  + "******************************************");
                ApiResponse<TransactionEntry> apiResponseCart = (new TransactionEntryService()).createTransactionEntry(transactionEntry);
                if (apiResponseCart.isValidResponse()) {
                    Log.i(TAG, "doInBackground: TransactionEntry successfully sent to the server");
                }
            }


            //testing. must send the entire transaction entry array in the actual implementation
            //TransactionEntry transactionEntry = (new TransactionEntry(ConfirmationScreenActivity.this.transactionEntryTransitionsCart.get(0)));
            // Log.i(TAG, "doInBackground: TransactionEntry id = " + transactionEntry.getRecordId().toString() + "////////");
            //transactionEntry.setRecordId(null);
            //ApiResponse<TransactionEntry> apiResponseCart = (new TransactionEntryService()).createTransactionEntry(transactionEntry);



            Log.i(TAG, "doInBackground: " + transactionTransition.getRecordId() + transactionTransition.getCashierId() + transactionTransition.getTotalAmount() + "******************************************");
            ApiResponse<Transaction> apiResponse = (new TransactionService()).updateTransaction(transaction);
            if (apiResponse.isValidResponse()) {
                processTransactionAlert.dismiss();

                Intent orderCompletePage = new Intent(ConfirmationScreenActivity.this, OrderComplete.class);
                orderCompletePage.putExtra("intent_extra_employee", ConfirmationScreenActivity.this.getIntent().getParcelableExtra("intent_extra_employee"));
                startActivity(orderCompletePage);
            }

            return apiResponse.isValidResponse();
        }

        @Override
        protected void onPostExecute(Boolean successful) {

        }


    }


}

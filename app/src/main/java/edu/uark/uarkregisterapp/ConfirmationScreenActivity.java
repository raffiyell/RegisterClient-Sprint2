package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
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
import java.util.List;
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
    private ArrayList<Product> products; //products from server
    private ArrayList<Product> updatedProducts; //products with updated count
    private double totPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        products = new ArrayList<>();
        updatedProducts = new ArrayList<>();
        (new QueryProductsTask()).execute();

        transactionTransition = getIntent().getParcelableExtra("intent_extra_transition");
        transactionEntryTransitionsCart = getIntent().getParcelableArrayListExtra("transactionEntryCart");

        ListView confirmationListView = findViewById(R.id.confirmationListView);
        productListAdapter = new ConfirmationListAdapter(this, transactionEntryTransitionsCart);
        confirmationListView.setAdapter(productListAdapter);

        TextView totalPriceTextView = findViewById(R.id.text_view_total_price_confirmation);
        getTotalPrice();
        updateTransaction();
        totalPriceTextView.setText("Total price: $" + getTotalPrice());

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

    //update the product counts
    private void updateProductCount() {
        Log.i(TAG, "updateProductCount: *************1000000000000*******");
        for (Product product : products) {
            for (TransactionEntryTransition temp : transactionEntryTransitionsCart) {
                if (product.getLookupCode().equals(temp.getLookupCode())) {
                    int newCount = product.getCount() - temp.getQuantity();
                    product.setCount(newCount);
                    updatedProducts.add(product);
                }
            }
        }
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
                Log.i(TAG, "doInBackground: transaction Reference id" + transactionEntry.getTransactionReferenceId() + "******************************************");
                ApiResponse<TransactionEntry> apiResponseCart = (new TransactionEntryService()).createTransactionEntry(transactionEntry);
                if (apiResponseCart.isValidResponse()) {
                    Log.i(TAG, "doInBackground: TransactionEntry successfully sent to the server");
                }

                ApiResponse<Product> apiResponseUpdatedProductInventory = (new ProductService()).updateProduct(updatedProducts.get(i));
                if (apiResponseUpdatedProductInventory.isValidResponse()) {
                    Log.i(TAG, "doInBackground: Product successfully updated");
                }
            }

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


    private class QueryProductsTask extends AsyncTask<Void, Void, ApiResponse<List<Product>>> {
        @Override
        protected ApiResponse<List<Product>> doInBackground(Void... params) {
            ApiResponse<List<Product>> apiResponse = (new ProductService()).getProducts();

            if (apiResponse.isValidResponse()) {

                products.clear();
                products.addAll(apiResponse.getData());
                Log.i(TAG, "doInBackground: successfully queried products*********************");
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<Product>> listApiResponse) {
            updateProductCount();

        }
    }


}

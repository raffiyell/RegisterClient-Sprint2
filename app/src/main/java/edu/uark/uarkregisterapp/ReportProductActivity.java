package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.api.services.TransactionEntryService;

public class ReportProductActivity extends AppCompatActivity {
    private static final String TAG = "ReportProductActivity";
    private ArrayList<TransactionEntry> transactionEntries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_product);

        (new RetrieveTransactionEntryTask()).execute();


    }


    private void sort(){

        //occurences
        int max= 0;
        TransactionEntry bestSelling = new TransactionEntry();for (int i = 0; i < transactionEntries.size(); i ++) {
            TransactionEntry temp = transactionEntries.get(i);

            int occurrences = Collections.frequency(transactionEntries, temp);
            if (occurrences > max) {
                Log.i(TAG, "sort: occurences = " + occurrences);
                max = occurrences;
                bestSelling = temp;
            }
        }

        Log.i(TAG, "sort: "+ bestSelling.getLookupCode() + "*********************");
    }

    private class RetrieveTransactionEntryTask extends AsyncTask<Void, Void, ApiResponse<List<TransactionEntry>>> {


        @Override
        protected ApiResponse<List<TransactionEntry>> doInBackground(Void... params) {
            ApiResponse<List<TransactionEntry>> apiResponse = (new TransactionEntryService()).getTransactionEntries();

            if (apiResponse.isValidResponse()) {
                transactionEntries.clear();
                transactionEntries.addAll(apiResponse.getData());
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<TransactionEntry>> apiResponse) {
            if (apiResponse.isValidResponse()) {
                sort();
            }

        }


    }

}

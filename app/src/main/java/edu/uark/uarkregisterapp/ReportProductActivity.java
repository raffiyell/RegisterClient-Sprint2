package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.uark.uarkregisterapp.adapters.ReportCashierListAdapter;
import edu.uark.uarkregisterapp.adapters.ReportProductListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.api.services.TransactionEntryService;
import edu.uark.uarkregisterapp.models.api.services.TransactionService;

public class ReportProductActivity extends AppCompatActivity {
    private static final String TAG = "ReportProductActivity";
    private ArrayList<TransactionEntry> transactionEntries;
    private ReportProductListAdapter reportProductListAdapter;
    private ArrayList<String> sortedTransactionEntryLookupCode;
    private ArrayList<Integer> sortedTransactionEntryCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_product);
        getSupportActionBar().hide();

        transactionEntries = new ArrayList<>();
        (new RetrieveTransactionEntryTask()).execute();
    }

    private void findBestSelling() {
        //occurences
        int max = 0;
        TransactionEntry bestSelling = new TransactionEntry();

        ArrayList<String> lookupCodes = new ArrayList<>();
        for (int i = 0; i < transactionEntries.size(); i++) {
            lookupCodes.add(transactionEntries.get(i).getLookupCode());
        }

        // hashmap to store the frequency of cashiers. key = cashierId, value = occurence
        Map<String, Integer> hm = new HashMap<String, Integer>();
        for (String k : lookupCodes) {
            Integer j = hm.get(k);
            hm.put(k, (j == null) ? 1 : j + 1);
        }


        for (int i = 0; i < transactionEntries.size(); i++) {
            TransactionEntry currentTransactionEntry = transactionEntries.get(i);
            Integer j = hm.get(currentTransactionEntry.getLookupCode());
            Log.i(TAG, "findBestSelling: lookup = " + currentTransactionEntry.getLookupCode() + " quantity =  " + currentTransactionEntry.getQuantity());
            hm.put(currentTransactionEntry.getLookupCode(), (j == null) ? currentTransactionEntry.getQuantity() : j + currentTransactionEntry.getQuantity()  ); //todo fix calculation
            Log.i(TAG, "findBestSelling: lookup = " + currentTransactionEntry.getLookupCode() + "tots = " + j  + currentTransactionEntry.getQuantity());
        }

        Map<String, Integer> sortedTransactionEntryMap = getSortedMapByValues(hm);
        sortedTransactionEntryLookupCode = new ArrayList<>(sortedTransactionEntryMap.keySet());
        sortedTransactionEntryCount = new ArrayList<>(sortedTransactionEntryMap.values());

        Log.i(TAG, "findBestSelling: sorted cashier =" + sortedTransactionEntryLookupCode.toString());
        Log.i(TAG, "findBestSelling: sorted cashier =" + sortedTransactionEntryCount.toString());
    }

    private static <K extends Comparable, V extends Comparable> Map<K, V> getSortedMapByValues(final Map<K, V> map) {
        Map<K, V> mapSortedByValues = new LinkedHashMap<K, V>();

        //get all the entries from the original map and put it in a List
        ArrayList<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        //sort the entries based on the value by custom Comparator
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> entry1, Map.Entry<K, V> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });

        //put all sorted entries in LinkedHashMap
        for (Map.Entry<K, V> entry : list)
            mapSortedByValues.put(entry.getKey(), entry.getValue());

        //return Map sorted by values
        return mapSortedByValues;
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
                findBestSelling();

                ListView productListView = findViewById(R.id.list_view_top_selling_products_id);
                ReportProductActivity.this.reportProductListAdapter = new ReportProductListAdapter(ReportProductActivity.this, ReportProductActivity.this.sortedTransactionEntryLookupCode, ReportProductActivity.this.sortedTransactionEntryCount);
                productListView.setAdapter(reportProductListAdapter);
            }

        }


    }

}

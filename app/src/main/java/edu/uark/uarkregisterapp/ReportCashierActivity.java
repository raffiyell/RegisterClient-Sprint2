package edu.uark.uarkregisterapp;

import android.os.AsyncTask;
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
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.services.TransactionEntryService;
import edu.uark.uarkregisterapp.models.api.services.TransactionService;

public class ReportCashierActivity extends AppCompatActivity {
    private static final String TAG = "ReportCashierActivity";
    private ArrayList<Transaction> transactions;
    private ReportCashierListAdapter reportCashierListAdapter;
    private ArrayList<String> sortedCashier;
    private ArrayList<Integer> sortedCashierTransactionCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_cashier);

        getSupportActionBar().hide();

        transactions = new ArrayList<>();
        (new RetrieveTransactionTask()).execute();

    }


    private void findBestSelling() {
        //occurences
        int max = 0;
        Transaction bestSelling = new Transaction();

        ArrayList<String> cashierIDs = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            cashierIDs.add(transactions.get(i).getCashierId());
        }

        // hashmap to store the frequency of cashiers. key = cashierId, value = occurence
        Map<String, Integer> hm = new HashMap<String, Integer>();
        for (String k : cashierIDs) {
            Integer j = hm.get(k);
            hm.put(k, (j == null) ? 1 : j + 1);
        }


        Map<String, Integer> sortedCashierMap = getSortedMapByValues(hm);
        sortedCashier = new ArrayList<>(sortedCashierMap.keySet());
        sortedCashierTransactionCount = new ArrayList<>(sortedCashierMap.values());

        Log.i(TAG, "findBestSelling: sorted cashier =" + sortedCashier.toString() );
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


    private class RetrieveTransactionTask extends AsyncTask<Void, Void, ApiResponse<List<Transaction>>> {


        @Override
        protected ApiResponse<List<Transaction>> doInBackground(Void... params) {
            ApiResponse<List<Transaction>> apiResponse = (new TransactionService()).getTransactions();

            if (apiResponse.isValidResponse()) {
                transactions.clear();
                transactions.addAll(apiResponse.getData());
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<Transaction>> apiResponse) {
            if (apiResponse.isValidResponse()) {
                findBestSelling();

                ListView cashierlistView = findViewById(R.id.list_view_cashier_id);
                ReportCashierActivity.this.reportCashierListAdapter = new ReportCashierListAdapter(ReportCashierActivity.this, ReportCashierActivity.this.sortedCashier, ReportCashierActivity.this.sortedCashierTransactionCount);
                cashierlistView.setAdapter(reportCashierListAdapter);

            }

        }


    }
}
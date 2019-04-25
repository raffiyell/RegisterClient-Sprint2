package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uark.uarkregisterapp.adapters.ProductListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;


public class ProductsListingActivity extends AppCompatActivity implements ProductListAdapter.ProductEntryCallback {
    private static final String TAG = "ProductsListingActivity";
    private EditText searchEditText;

    private ProductListAdapter.ProductEntryCallback productEntryCallback;
    private ArrayList<Product> productsInCart;
    private ArrayList<TransactionEntryTransition> transactionEntriesTransition;
    private Transaction transaction;

    /**
     * Very unsure about the logic so this may likely change:
     * This is the logic behind the creation of a Transaction
     * When a transaction is started(or the ProductsListingActivity starts), a transaction object will be created. Each product added(when the "add" button is clicked) to the cart will create a transaction entry object.
     * Each transaction entry object will reference the same transaction reference id that was created from the transaction object.
     * changing the quantity in each entry in the products list will change the transaction entry objects to modify its individual quantity and individual total price (total price = quantity * product price) of the same product.
     * This does not include the entire price of all the products in the cart, just the ones of the same product.
     * The entire price of all the products in the cart will be summed up in the total amount variable in the transaction object.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ****************************************");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_listing);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = this.getSupportActionBar();
        // todo fix bug. employee name is not saved in the main activity. the name is usually passed as an employeeTransition object variable. When the back button in toolbar of the products list is clicked, there is no employeeTransition passed to the activity
        //fix by either requesting the name again from the server or save the name in the login screen as a SharedPreference
        // if (actionBar != null) {
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //}

        createTransaction();

        this.products = new ArrayList<>(); //products in inventory
        this.productsInCart = new ArrayList<>(); // products added in cart
        this.productListAdapter = new ProductListAdapter(this, this.products, this); //"this" is casted into ProductEntryCallback

        this.getProductsListView().setAdapter(this.productListAdapter);
        this.getProductsListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);
                intent.putExtra(
                        getString(R.string.intent_extra_product),
                        new ProductTransition((Product) getProductsListView().getItemAtPosition(position))
                );
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        (new RetrieveProductsTask()).execute();
    }

    private void createTransaction() {
        Log.d(TAG, "createTransaction: *******************************************************");

        transaction = new Transaction();
        //String cashierId = this.getIntent().getParcelableExtra("intent_employeeid");
        //transaction.setCashierId(cashierId);
        this.transactionEntriesTransition = new ArrayList<>();

    }

    private ListView getProductsListView() {
        return (ListView) this.findViewById(R.id.list_view_products_id);
    }

    public void searchButton(View view) {
        searchEditText = findViewById(R.id.editTextSearchById);
        String searchId = searchEditText.getText().toString();
        //	searchId.addTextChangedListener
        (new searchingTask(searchId)).execute();
    }

    //from ProductEntryCallback interface
    @Override
    public void onProductEntryAdd(int position) { //productCount is the one specified by the user, using the plus/minus buttons.
        Log.d(TAG, "onProductEntryAdd: ***************************************");
        Toast.makeText(ProductsListingActivity.this, "test add product at position " + position, Toast.LENGTH_SHORT).show();
        productsInCart.add(this.products.get(position));

        TransactionEntryTransition newTransactionEntry = new TransactionEntryTransition(transaction.getId(), products.get(position).getLookupCode(), products.get(position).getPrice()); //quantity will be added when the next button is clicked
        transactionEntriesTransition.add(newTransactionEntry);

        //Toast.makeText(this, productsInCart.toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "TransactionEntryTransition   " +
                        transactionEntriesTransition.get(0).getRecordId() + "   " +
                        transactionEntriesTransition.get(0).getLookupCode() + "  " +
                        transactionEntriesTransition.get(0).getPrice() + " " +
                        transactionEntriesTransition.get(0).getQuantity() + " " +
                        transactionEntriesTransition.get(0).getTransactionReferenceId()
                , Toast.LENGTH_SHORT).show();


        //todo finish. send TransactionEntries to the confirmation activity
        //todo figure out the logic behind recordId's. if it is initialized to 0000-0000.... what is going to be sent to the json file? how does the server handle this?
        //todo fix recordId in server? is the actual recordId created in the server
        //todo send post request to add a Transaction entry to the database and return its record id. This record id will then be used as the TransactionReferenceId for each transactionEntry object created
        //todo modify the count and the newPrice in each entry in the transactionEntriesTransition array list
        Log.i(TAG, "onProductEntryAdd: ");
    }

    //from ProductEntryCallback interface
    @Override
    public void onProductEntryRemove(String lookupCode) {
        Toast.makeText(this, productsInCart.toString(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < productsInCart.size(); i++) {
            if (productsInCart.get(i).getLookupCode().equals(lookupCode)) {
                productsInCart.remove(i);
            }
        }

    }

    @Override
    public void onProductEntryQuantityUpdate(String lookupCode, int quantity) {
        Log.i(TAG, "onProductEntryQuantityUpdate: " + lookupCode + " " + quantity + " ************************");
        for (int i = 0; i < transactionEntriesTransition.size(); i++) {
            if (transactionEntriesTransition.get(i).getLookupCode().equals(lookupCode)) {
                transactionEntriesTransition.get(i).setQuantity(quantity);
                Log.i(TAG, "onProductEntryQuantityUpdate: lookupcode=" + transactionEntriesTransition.get(i).getLookupCode() + " quantity = " + transactionEntriesTransition.get(i).getQuantity() + " *******************************");
            }
        }
    }

    public void productListNextFAB(View view) {
        Intent confirmationPage = new Intent(ProductsListingActivity.this, ConfirmationScreenActivity.class);

        confirmationPage.putParcelableArrayListExtra("transactionEntryCart", transactionEntriesTransition);
        startActivity(confirmationPage);
    }

    ///////////////////////////////////////// In progress {
    private class searchingTask extends AsyncTask<Void, Void, ApiResponse<Product>> {
        String searchID;

        @Override
        protected void onPreExecute() {
            this.searchingProductsAlert.show();
        }

        @Override
        protected ApiResponse<Product> doInBackground(Void... params) {
            ApiResponse<Product> apiResponse = (new ProductService()).getProductByLookupCode(searchID);

            if (apiResponse.isValidResponse()) {
                products.clear();
                products.add(apiResponse.getData());
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<Product> apiResponse) {
            if (apiResponse.isValidResponse()) {
                productListAdapter.notifyDataSetChanged();
            }

            this.searchingProductsAlert.dismiss();

            if (!apiResponse.isValidResponse()) {
                new AlertDialog.Builder(ProductsListingActivity.this).
                        setMessage("unable to search products").
                        setPositiveButton(
                                R.string.button_dismiss,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }
                        ).
                        create().
                        show();
            }
        }

        private AlertDialog searchingProductsAlert;

        private searchingTask(String id) {
            searchID = id;
            this.searchingProductsAlert = new AlertDialog.Builder(ProductsListingActivity.this).
                    setMessage("searching products").
                    create();
        }
    }

//////////////////////////////////////////////// }


    private class RetrieveProductsTask extends AsyncTask<Void, Void, ApiResponse<List<Product>>> {
        @Override
        protected void onPreExecute() {
            this.loadingProductsAlert.show();
        }

        @Override
        protected ApiResponse<List<Product>> doInBackground(Void... params) {
            ApiResponse<List<Product>> apiResponse = (new ProductService()).getProducts();

            if (apiResponse.isValidResponse()) {
                products.clear();
                products.addAll(apiResponse.getData());
            }

            return apiResponse;
        }

        @Override
        protected void onPostExecute(ApiResponse<List<Product>> apiResponse) {
            if (apiResponse.isValidResponse()) {
                productListAdapter.notifyDataSetChanged();
            }

            this.loadingProductsAlert.dismiss();

            if (!apiResponse.isValidResponse()) {
                new AlertDialog.Builder(ProductsListingActivity.this).
                        setMessage(R.string.alert_dialog_products_load_failure).
                        setPositiveButton(
                                R.string.button_dismiss,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                }
                        ).
                        create().
                        show();
            }
        }

        private AlertDialog loadingProductsAlert;

        private RetrieveProductsTask() {
            this.loadingProductsAlert = new AlertDialog.Builder(ProductsListingActivity.this).
                    setMessage(R.string.alert_dialog_products_loading).
                    create();
        }
    }

    private List<Product> products;
    private ProductListAdapter productListAdapter;
}

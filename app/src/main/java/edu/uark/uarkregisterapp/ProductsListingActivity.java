package edu.uark.uarkregisterapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.uark.uarkregisterapp.adapters.ProductListAdapter;
import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.services.ProductService;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;


public class ProductsListingActivity extends AppCompatActivity implements ProductListAdapter.ProductEntryCallback {
    private EditText searchEditText;
    private ProductListAdapter.ProductEntryCallback productEntryCallback;
    private ArrayList<Product> productsInCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_listing);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
    public void onProductEntryAdd(int position) {
        Toast.makeText(ProductsListingActivity.this, "test add product at position " + position, Toast.LENGTH_SHORT).show();
        productsInCart.add(this.products.get(position));
        Toast.makeText(this, productsInCart.toString(), Toast.LENGTH_SHORT).show();

    }

    //from ProductEntryCallback interface
    @Override
    public void onProductEntryRemove(String lookupCode) {

        Toast.makeText(this, productsInCart.toString(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < productsInCart.size(); i++){
            if (productsInCart.get(i).getLookupCode().equals(lookupCode))
            {
                productsInCart.remove(i);
            }
        }

    }

    public void productListNextFAB(View view) {
        Intent confirmationPage = new Intent(ProductsListingActivity.this, ConfirmationScreenActivity.class);
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

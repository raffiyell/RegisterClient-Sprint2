package edu.uark.uarkregisterapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.uark.uarkregisterapp.adapters.ConfirmationListAdapter;
import edu.uark.uarkregisterapp.adapters.ProductListAdapter;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.transition.ProductTransition;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;

public class ConfirmationScreenActivity extends AppCompatActivity {
    private ArrayList<Product> productsInCart;
    private ConfirmationListAdapter productListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        ArrayList<ProductTransition> cart = getIntent().getParcelableArrayListExtra("cart");
      //  ArrayList<TransactionEntryTransition> transactionEntryTransitions = getIntent().getParcelableArrayListExtra("transactionEntryCart");

        productsInCart = new ArrayList<>();

        for (int i = 0; i < cart.size(); i ++){
            productsInCart.add(new Product(cart.get(i)));
        }

        Toast.makeText(ConfirmationScreenActivity.this, productsInCart.toString(), Toast.LENGTH_SHORT).show();
    //    Toast.makeText(ConfirmationScreenActivity.this, transactionEntryTransitions.toString(), Toast.LENGTH_SHORT).show();
        ListView confirmationListView = findViewById(R.id.confirmationListView);


        productListAdapter = new ConfirmationListAdapter(this, productsInCart);
        confirmationListView.setAdapter(productListAdapter);


    }


}

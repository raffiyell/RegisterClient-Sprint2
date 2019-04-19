package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.Product;

public class ConfirmationListAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "ConfirmationListAdapter";
    private int productCount;


    public ConfirmationListAdapter(Context context, List<Product> cart) {
        super(context, R.layout.list_view_item_cart_confirmation, cart);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.list_view_item_cart_confirmation, parent, false);
        }

        Product product = this.getItem(position);
        if (product != null) {
            TextView lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_lookup_code_confirmation);
            if (lookupCodeTextView != null) {
               lookupCodeTextView.setText(product.getLookupCode());
            }

            TextView priceTextView = (TextView) view.findViewById(R.id.list_view_item_price_confirmation);
            if (priceTextView != null) {
                priceTextView.setText(product.getPrice() + " ");
            }
        }
        return view;
    }



}
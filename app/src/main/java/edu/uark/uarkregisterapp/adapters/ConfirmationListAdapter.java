package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;

public class ConfirmationListAdapter extends ArrayAdapter<TransactionEntryTransition> {

    private static final String TAG = "ConfirmationListAdapter";
    private int productCount;


    public ConfirmationListAdapter(Context context, List<TransactionEntryTransition> cart) {
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

        TransactionEntryTransition transactionEntryTransition = this.getItem(position);
        if (transactionEntryTransition != null) {
            TextView lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_lookup_code_confirmation);
            if (lookupCodeTextView != null) {
                lookupCodeTextView.setText(transactionEntryTransition.getLookupCode());
            }

            TextView priceTextView = (TextView) view.findViewById(R.id.list_view_item_price_confirmation);
            if (priceTextView != null) {
                priceTextView.setText("$ " + transactionEntryTransition.getPrice() + " ");
            }

            TextView quantityTextView = view.findViewById(R.id.list_view_item_quantity_confirmation);
            if (quantityTextView != null) {
                quantityTextView.setText("Qty: " + transactionEntryTransition.getQuantity() + " ");
            }

            TextView totalPriceTextView = (TextView) view.findViewById(R.id.list_view_item_total_price_confirmation);
            if (totalPriceTextView != null) {

                double totPrice = (transactionEntryTransition.getPrice() * transactionEntryTransition.getQuantity());

                totalPriceTextView.setText("Total: $ " + totPrice); //todo fix decimal value to two digits
            }

        }
        return view;
    }


}
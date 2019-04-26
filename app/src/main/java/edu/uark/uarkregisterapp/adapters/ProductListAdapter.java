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
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private static final String TAG = "ProductListAdapter";
    private List<TransactionEntryTransition> transactionEntryTransitionCart;
    private Transaction transaction;

    public ProductListAdapter(Context context, List<Product> products, Transaction transaction, List<TransactionEntryTransition> transactionEntryTransition) {
        super(context, R.layout.list_view_item_product, products);
        this.transaction = transaction;
        this.transactionEntryTransitionCart = transactionEntryTransition;
    }

    public ProductListAdapter(Context context, List<Product> products) {
        super(context, R.layout.list_view_item_product, products);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        Product product;
        product = this.getItem(position); //todo fix: only one product and one productCount is stored in the entire listview, instead of one product and one productCount on each entry in the list
        View view = convertView;

        final ViewHolder holder;
        holder = new ViewHolder();
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.list_view_item_product, parent, false);

            holder.lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_product_lookup_code);
            holder.priceTextView = view.findViewById(R.id.list_view_item_price);
            holder.cartProductQuantity = view.findViewById(R.id.cart_product_quantity);
            holder.addCardView = view.findViewById(R.id.incrementCardView);
            holder.minusCardView = view.findViewById(R.id.decrementCardView);
            holder.removeProductCardView = view.findViewById(R.id.removeProductCardView);
            holder.addProductCardView = view.findViewById(R.id.addProductCardView);

            holder.cartProductQuantity.setVisibility(View.INVISIBLE);
            holder.addCardView.setVisibility(View.INVISIBLE);
            holder.minusCardView.setVisibility(View.INVISIBLE);

            holder.addCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransactionEntryTransition transactionEntryTemp = transactionEntryTransitionCart.get(getPositionFromCart(ProductListAdapter.this.getItem(position).getLookupCode()));
                    Log.d(TAG, "onClick: *******************************************");
                    int newQuantity = transactionEntryTemp.getQuantity() + 1;
                    transactionEntryTemp.setQuantity(newQuantity);
                    holder.cartProductQuantity.setText(transactionEntryTemp.getQuantity() + " ");
                }
            });
            holder.minusCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransactionEntryTransition transactionEntryTemp = transactionEntryTransitionCart.get(getPositionFromCart(ProductListAdapter.this.getItem(position).getLookupCode()));
                    Log.d(TAG, "onClick: *******************************************");
                    int newQuantity = transactionEntryTemp.getQuantity() - 1;
                    if (newQuantity < 1)
                        newQuantity = 1;
                    transactionEntryTemp.setQuantity(newQuantity);
                    holder.cartProductQuantity.setText(transactionEntryTemp.getQuantity() + "");
                }
            });

            holder.removeProductCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product tempProduct = ProductListAdapter.this.getItem(position);

                    if (!cartHasProduct(tempProduct.getLookupCode())) { // makes sure nothing is removed if the product count is already 0. product is only removed in the cart if it is already added
                        Log.i(TAG, "onClick: product not found in cart. cannot remove anything ************");
                        //do nothing
                    } else {
                        TransactionEntryTransition transactionEntryTemp = transactionEntryTransitionCart.get(getPositionFromCart(tempProduct.getLookupCode()));
                        transactionEntryTransitionCart.remove(getPositionFromCart(tempProduct.getLookupCode()));
                        holder.addCardView.setVisibility(View.INVISIBLE);
                        holder.minusCardView.setVisibility(View.INVISIBLE);
                        holder.cartProductQuantity.setVisibility(View.INVISIBLE);
                        holder.cartProductQuantity.setText(transactionEntryTemp.getQuantity() + "");
                    }
                }
            });

            holder.addProductCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TransactionEntryTransition newTransactionEntry = new TransactionEntryTransition(transaction.getId(),
                            ProductListAdapter.this.getItem(position).getLookupCode(),
                            ProductListAdapter.this.getItem(position).getPrice()); //quantity will be added when the next button is clicked

                    if (cartHasProduct(newTransactionEntry.getLookupCode()))
                    {
                        Log.i(TAG, "onClick: product already in cart *************************");
                        //product already in cart.
                        //do nothing to prevent redundancy
                    } else {
                        if (newTransactionEntry.getQuantity() <= 0) {
                            newTransactionEntry.setQuantity(1);
                            transactionEntryTransitionCart.add(newTransactionEntry);
                            holder.addCardView.setVisibility(View.VISIBLE);
                            holder.minusCardView.setVisibility(View.VISIBLE);
                            holder.cartProductQuantity.setVisibility(View.VISIBLE);
                        } else {
                            //do nothing.
                            holder.addCardView.setVisibility(View.VISIBLE);
                            holder.minusCardView.setVisibility(View.VISIBLE);
                            holder.cartProductQuantity.setVisibility(View.VISIBLE);
                        }
                        holder.cartProductQuantity.setText(newTransactionEntry.getQuantity() + " ");
                    }
                }
            });
        }

        if (product != null) {
            if (holder.lookupCodeTextView != null) {
                holder.lookupCodeTextView.setText(product.getLookupCode());
            }

            holder.countTextView = (TextView) view.findViewById(R.id.list_view_item_product_count);
            if (holder.countTextView != null) {
                holder.countTextView.setText(String.format(Locale.getDefault(), "%d", product.getCount()));
            }

            if (holder.priceTextView != null) {
                holder.priceTextView.setText("$ " + product.getPrice() + " ");
            }
        }
        return view;
    }

    private int getPositionFromCart(String lookupCode){
        for (TransactionEntryTransition temp: transactionEntryTransitionCart) {
            if(temp.getLookupCode().equals(lookupCode))
                return transactionEntryTransitionCart.indexOf(temp);
        }
        return -1;
    }

    private boolean cartHasProduct(String lookupCode) {
        for (TransactionEntryTransition temp: transactionEntryTransitionCart) {
            if(temp.getLookupCode().equals(lookupCode))
                return true;
        }
        return false;
    }

    static class ViewHolder {
        public TextView lookupCodeTextView;
        public TextView countTextView;
        public TextView priceTextView;
        public CardView addCardView;
        public CardView minusCardView;
        public TextView cartProductQuantity;
        public CardView removeProductCardView;
        public CardView addProductCardView;
    }


}

package edu.uark.uarkregisterapp.adapters;

import android.app.Activity;
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

public class ProductListAdapter extends ArrayAdapter<Product> {
    private static final String TAG = "ProductListAdapter";
    private ProductEntryCallback productEntryCallback;
    private int productCount;
    private Product product;

    public ProductListAdapter(Context context, List<Product> products, ProductEntryCallback productEntryCallback) {
        super(context, R.layout.list_view_item_product, products);
        this.productEntryCallback = productEntryCallback;
    }

    public ProductListAdapter(Context context, List<Product> products) {
        super(context, R.layout.list_view_item_product, products);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        product = this.getItem(position);
        productCount = 0;
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
                    Log.d(TAG, "onClick: *******************************************");
                    increment();
                    holder.cartProductQuantity.setText(productCount + "");
                }
            });
            holder.minusCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: *******************************************");
                    decrement();
                    holder.cartProductQuantity.setText(productCount + "");
                }
            });

            final String lookupCode = product.getLookupCode();
            holder.removeProductCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productCount <= 0) {
                        holder.addCardView.setVisibility(View.INVISIBLE);
                        holder.minusCardView.setVisibility(View.INVISIBLE);
                        holder.cartProductQuantity.setVisibility(View.INVISIBLE);
                    } else { // makes sure nothing is removed if the product count is already 0. product is only removed in the cart if it is already added
                        removeProductFromCart();
                        holder.addCardView.setVisibility(View.INVISIBLE);
                        holder.minusCardView.setVisibility(View.INVISIBLE);
                        holder.cartProductQuantity.setVisibility(View.INVISIBLE);
                        productEntryCallback.onProductEntryRemove(lookupCode);
                    }

                    holder.cartProductQuantity.setText(productCount + "");
                }
            });

            holder.addProductCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productCount <= 0) {
                        productCount = 1;
                        productEntryCallback.onProductEntryAdd(position, productCount); //adds the product into the cart. implemented in ProductsListingActivity
                        holder.addCardView.setVisibility(View.VISIBLE);
                        holder.minusCardView.setVisibility(View.VISIBLE);
                        holder.cartProductQuantity.setVisibility(View.VISIBLE);

                    } else {
                        //do nothing.
                        holder.addCardView.setVisibility(View.VISIBLE);
                        holder.minusCardView.setVisibility(View.VISIBLE);
                        holder.cartProductQuantity.setVisibility(View.VISIBLE);
                    }

                    holder.cartProductQuantity.setText(productCount + "");

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
                holder.priceTextView.setText(product.getPrice() + " ");
            }

        }
        return view;
    }


    private void increment() {
        productCount++;
        productEntryCallback.onProductEntryQuantityUpdate(product.getLookupCode(), productCount);
    }

    private void decrement() {
        productCount--;
        if (productCount < 0)
            productCount = 0;

        productEntryCallback.onProductEntryQuantityUpdate(product.getLookupCode(), productCount);
    }

    private void removeProductFromCart() {
        productCount = 0;

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

    public interface ProductEntryCallback {
        void onProductEntryAdd(int position, int productCount);

        void onProductEntryRemove(String lookupCode);

        void onProductEntryQuantityUpdate(String lookupCode, int quantity);

    }

}

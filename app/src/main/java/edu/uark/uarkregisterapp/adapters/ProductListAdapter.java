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
	int productCount = 0;


	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {

		View view = convertView;
		final ViewHolder holder;
		holder = new ViewHolder();
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			view = inflater.inflate(R.layout.list_view_item_product, parent, false);


			holder.lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_product_lookup_code);
			holder.cartProductQuantity = view.findViewById(R.id.cart_product_quantity);
			holder.addCardView = view.findViewById(R.id.incrementCardView);
			holder.minusCardView = view.findViewById(R.id.decrementCardView);
			holder.removeProductCardView = view.findViewById(R.id.removeProductCardView);


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

			holder.removeProductCardView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					removeProductFromCart();
					holder.cartProductQuantity.setText(productCount + "");
				}
			});
		}

		
		Product product = this.getItem(position);
		if (product != null) {

			if (holder.lookupCodeTextView != null) {
				holder.lookupCodeTextView.setText(product.getLookupCode());
			}

			holder.countTextView = (TextView) view.findViewById(R.id.list_view_item_product_count);
			if (holder.countTextView != null) {
				holder.countTextView.setText(String.format(Locale.getDefault(), "%d", product.getCount()));
			}

		}
		return view;
	}

	public ProductListAdapter(Context context, List<Product> products) {
		super(context, R.layout.list_view_item_product, products);
	}

	private void increment() {
		productCount++;
	}

	private void decrement() {
		productCount--;
		if (productCount < 0)
			productCount = 0;
	}

	private void removeProductFromCart(){
		productCount = 0;
	}

	static class ViewHolder {
		public TextView lookupCodeTextView;
		TextView countTextView;
		public CardView addCardView;
		public CardView minusCardView;
		public TextView cartProductQuantity;
		public CardView removeProductCardView;


	}
	
}

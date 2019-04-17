package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import edu.uark.uarkregisterapp.R;
import edu.uark.uarkregisterapp.models.api.Product;

public class ProductListAdapter extends ArrayAdapter<Product> {
	int productCount = 0;


	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {


		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(this.getContext());
			view = inflater.inflate(R.layout.list_view_item_product, parent, false);
		}



		CardView addCardView = view.findViewById(R.id.increment_CardView);
		addCardView.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				increment();

			}
		});



		Product product = this.getItem(position);
		if (product != null) {


			TextView lookupCodeTextView = (TextView) view.findViewById(R.id.list_view_item_product_lookup_code);
			if (lookupCodeTextView != null) {
				lookupCodeTextView.setText(product.getLookupCode());
			}

			TextView countTextView = (TextView) view.findViewById(R.id.list_view_item_product_count);
			if (countTextView != null) {
				countTextView.setText(String.format(Locale.getDefault(), "%d", product.getCount()));
			}


			TextView cartProductQuantity = view.findViewById(R.id.cart_product_quantity);
			cartProductQuantity.setText(productCount + "32");
//todo FIX

		}

		return view;
	}

	public ProductListAdapter(Context context, List<Product> products) {
		super(context, R.layout.list_view_item_product, products);
	}

	public void increment()
	{
		productCount++;
	}
}

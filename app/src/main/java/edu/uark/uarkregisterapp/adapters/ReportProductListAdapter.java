package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.uark.uarkregisterapp.R;

public class ReportProductListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "ReportProductAdapter";
    ArrayList<Integer> transactionEntryQuantity;

    public ReportProductListAdapter(Context context, ArrayList<String> lookupcodes, ArrayList<Integer> transactionEntryQuantity) {
        super(context, R.layout.list_view_item_report_product, lookupcodes);
        this.transactionEntryQuantity = transactionEntryQuantity; //# of product sold for a particular lookupcode

    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.list_view_item_report_product, parent, false);
        }

        String lookupcode = this.getItem(position);
        int currentProductSold = transactionEntryQuantity.get(position);

        if (lookupcode != null && !lookupcode.isEmpty()) {
            TextView cashierIdTextView = (TextView) view.findViewById(R.id.text_view_product_lookup_code_report);
            cashierIdTextView.setText(lookupcode + " sold " + (currentProductSold - 1) + " items"); //subtracting 1 is a temporary fix. not the right way
        }

        return view;
    }
}

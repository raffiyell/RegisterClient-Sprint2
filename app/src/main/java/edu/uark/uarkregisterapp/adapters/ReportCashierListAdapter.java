package edu.uark.uarkregisterapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.uark.uarkregisterapp.R;

public class ReportCashierListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "ReportCashierAdapter";
    ArrayList<Integer> cashierCounts;

    public ReportCashierListAdapter(Context context, ArrayList<String> cashierIds, ArrayList<Integer> cashierCounts) {
        super(context, R.layout.list_view_item_cashiers, cashierIds);
        this.cashierCounts = cashierCounts; //# of transactions the cashier made
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.list_view_item_cashiers, parent, false);
        }

        String cashierId = this.getItem(position);
        int currentCashierCount = cashierCounts.get(position);
        Log.i(TAG, "getView: " + cashierId);

        if (cashierId != null && !cashierId.isEmpty()) {
            TextView cashierIdTextView = (TextView) view.findViewById(R.id.text_view_cashierId_report);
           cashierIdTextView.setText("Cashier " + cashierId + " made " + currentCashierCount+ " transactions");
        }

        return view;
    }


}

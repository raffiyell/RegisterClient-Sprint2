package edu.uark.uarkregisterapp.models.api.services;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Product;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;
import edu.uark.uarkregisterapp.models.api.enums.ApiObject;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiMethod;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class TransactionEntryService extends BaseRemoteService {
    private static final String TAG = "TransactionEntryService";

    public ApiResponse<TransactionEntry> getTransactionEntry(UUID TransactionEntryId) {
        return this.readTransactionEntryDetailsFromResponse(
                this.<TransactionEntry>performGetRequest(
                        this.buildPath(TransactionEntryId)
                )
        );
    }

    //only for testing. will not be used officially
    public ApiResponse<List<TransactionEntry>> getTransactionEntries() {
        ApiResponse<List<TransactionEntry>> apiResponse = this.performGetRequest(
                this.buildPath()
        );

        JSONArray rawJsonArray = this.rawResponseToJSONArray(apiResponse.getRawResponse());
        if (rawJsonArray != null) {
            ArrayList<TransactionEntry> transactionEntries = new ArrayList<>(rawJsonArray.length());
            for (int i = 0; i < rawJsonArray.length(); i++) {
                try {
                    transactionEntries.add((new TransactionEntry()).loadFromJson(rawJsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.d(TAG, e.getMessage());
                }
            }

            apiResponse.setData(transactionEntries);
        } else {
            apiResponse.setData(new ArrayList<TransactionEntry>(0));
        }

        return apiResponse;
    }


    public ApiResponse<TransactionEntry> updateTransactionEntry(TransactionEntry transactionEntry) {
        return this.readTransactionEntryDetailsFromResponse(
                this.<TransactionEntry>performPutRequest(
                        this.buildPath(transactionEntry.getRecordId()),
                        transactionEntry.convertToJson()
                )
        );
    }

    public ApiResponse<TransactionEntry> createTransactionEntry(TransactionEntry transactionEntry) {
        return this.readTransactionEntryDetailsFromResponse(
                this.<TransactionEntry>performPostRequest(
                        this.buildPath(),
                        transactionEntry.convertToJson()
                )
        );
    }

    public ApiResponse<String> deleteTransactionEntry(UUID transactionEntryId) {
        return this.<String>performDeleteRequest(
                this.buildPath(transactionEntryId)
        );
    }

    private ApiResponse<TransactionEntry> readTransactionEntryDetailsFromResponse(ApiResponse<TransactionEntry> apiResponse) {
        return this.readDetailsFromResponse(
                apiResponse, (new TransactionEntry())
        );
    }

    private <T extends LoadFromJsonInterface<T>> ApiResponse<T> readDetailsFromResponse(ApiResponse<T> apiResponse, T apiObject) {
        JSONObject rawJsonObject = this.rawResponseToJSONObject(
                apiResponse.getRawResponse()
        );

        if (rawJsonObject != null) {
            apiResponse.setData(
                    apiObject.loadFromJson(rawJsonObject)
            );
        }

        return apiResponse;
    }

    public TransactionEntryService() { super(ApiObject.TRANSACTION_ENTRY); }
}

package edu.uark.uarkregisterapp.models.api.services;

import org.json.JSONObject;

import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.ApiResponse;
import edu.uark.uarkregisterapp.models.api.Transaction;
import edu.uark.uarkregisterapp.models.api.enums.ApiObject;
import edu.uark.uarkregisterapp.models.api.enums.TransactionApiMethod;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public class TransactionService extends BaseRemoteService {
	public ApiResponse<Transaction> getTransaction(UUID TransactionId) {
		return this.readTransactionDetailsFromResponse(
			this.<Transaction>performGetRequest(
				this.buildPath(TransactionId)
			)
		);
	}


	public ApiResponse<Transaction> updateTransaction(Transaction transaction) {
		return this.readTransactionDetailsFromResponse(
			this.<Transaction>performPutRequest(
				this.buildPath(transaction.getId()),
					transaction.convertToJson()
			)
		);
	}

	public ApiResponse<Transaction> createTransaction(Transaction transaction) {
		return this.readTransactionDetailsFromResponse(
			this.<Transaction>performPostRequest(
				this.buildPath(),
				transaction.convertToJson()
			)
		);
	}

	public ApiResponse<String> deleteTransaction(UUID transactionId) {
		return this.<String>performDeleteRequest(
			this.buildPath(transactionId)
		);
	}


	private ApiResponse<Transaction> readTransactionDetailsFromResponse(ApiResponse<Transaction> apiResponse) {
		return this.readDetailsFromResponse(
			apiResponse, (new Transaction())
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

	public TransactionService() { super(ApiObject.TRANSACTION); }
}

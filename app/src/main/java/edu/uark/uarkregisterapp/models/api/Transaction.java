package edu.uark.uarkregisterapp.models.api;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.enums.EmployeeClassification;
import edu.uark.uarkregisterapp.models.api.fields.EmployeeFieldName;
import edu.uark.uarkregisterapp.models.api.fields.TransactionFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;

public class Transaction  implements ConvertToJsonInterface, LoadFromJsonInterface<Transaction> {
    private UUID recordId;
    public UUID getRecordId() {
        return recordId;
    }
    public Transaction setRecordId(UUID recordId) {
        this.recordId = recordId;
        return this;
    }

    private String cashierId; // todo check if it should be  a string to reference employeeId or a UUID
    public String getCashierId() {
        return cashierId;
    }
    public Transaction setCashierId(String cashierId) {
        this.cashierId = cashierId;
        return this;
    }

    private double totalAmount;
    public double getTotalAmount() {
        return totalAmount;
    }
    public Transaction setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    private String transactionType;
    public String getTransactionType() {
        return transactionType;
    }
    public Transaction setTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    private UUID referenceId; //change data type as needed
    public UUID getReferenceId() {
        return referenceId;
    }
    public Transaction setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return createdOn;
    }
    public Transaction setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    private int totalItemSold; //total items sold may not be needed
    public int getTotalItemSold() {
        return totalItemSold;
    }
    public Transaction setTotalItemSold(int totalItemSold) {
        this.totalItemSold = totalItemSold;
        return this;
    }




    @Override
    public Transaction loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionFieldName.RECORD_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.recordId = UUID.fromString(value);
        }

        this.cashierId = rawJsonObject.optString(TransactionFieldName.CASHIER_ID.getFieldName());
        this.totalAmount = rawJsonObject.optDouble(TransactionFieldName.TOTAL_AMOUNT.getFieldName());
        this.transactionType = rawJsonObject.optString(TransactionFieldName.TRANSACTION_TYPE.getFieldName());


        value = rawJsonObject.optString(TransactionFieldName.REFERENCE_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.referenceId = UUID.fromString(value);
        }


        value = rawJsonObject.optString(EmployeeFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        this.totalItemSold = rawJsonObject.optInt(TransactionFieldName.TOTAL_ITEMS_SOLD.getFieldName()); //todo remove if not needed

        return this;
    }

    //todo fix convertToJson() and loadFromJson()
    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionFieldName.RECORD_ID.getFieldName(), this.recordId.toString());
            jsonObject.put(TransactionFieldName.CASHIER_ID.getFieldName(), this.cashierId);
            jsonObject.put(TransactionFieldName.TOTAL_AMOUNT.getFieldName(), this.totalAmount);
            jsonObject.put(TransactionFieldName.TRANSACTION_TYPE.getFieldName(), this.transactionType);
            jsonObject.put(TransactionFieldName.REFERENCE_ID.getFieldName(), this.referenceId);
            jsonObject.put(TransactionFieldName.CREATED_ON.getFieldName(), (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)).format(this.createdOn));
            jsonObject.put(TransactionFieldName.TOTAL_ITEMS_SOLD.getFieldName(), this.totalItemSold);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public Transaction() {
        this.recordId = new UUID(0, 0);
        this.createdOn = new Date();
        this.cashierId = StringUtils.EMPTY;
        this.totalAmount= 0.0;
        this.transactionType = StringUtils.EMPTY;
        this.referenceId = new UUID(0, 0);
        this.totalItemSold = 0;
    }
}

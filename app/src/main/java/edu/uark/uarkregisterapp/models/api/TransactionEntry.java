package edu.uark.uarkregisterapp.models.api;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.fields.TransactionEntryFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;
import edu.uark.uarkregisterapp.models.transition.TransactionEntryTransition;

public class TransactionEntry implements ConvertToJsonInterface, LoadFromJsonInterface<TransactionEntry> {

    private UUID recordId;
    public UUID getRecordId() {
        return recordId;
    }
    public TransactionEntry setRecordId(UUID recordId) {
        this.recordId = recordId;
        return this;
    }

    private String productLookupCode;
    public String getLookupCode() {
        return productLookupCode;
    }
    public TransactionEntry setLookupCode(String productLookupCode) {
        this.productLookupCode = productLookupCode;
        return this;
    }

    private int quantity;
    public int getQuantity() {
        return quantity;
    }
    public TransactionEntry setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    private double price;
    public double getPrice() {
        return price;
    }
    public TransactionEntry setPrice(double price) {
        this.price = price;
        return this;
    }

    private UUID transactionReferenceId;
    public UUID getTransactionReferenceId() {
        return transactionReferenceId;
    }
    public TransactionEntry setTransactionReferenceId(UUID transactionReferenceId) {
        this.transactionReferenceId = transactionReferenceId;
        return this;
    }
    private Date createdOn;
    public Date getCreatedOn() {
        return createdOn;
    }
    public TransactionEntry setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }


    @Override
    public TransactionEntry loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionEntryFieldName.RECORD_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.recordId = UUID.fromString(value);
        }

        this.productLookupCode = rawJsonObject.optString(TransactionEntryFieldName.PRODUCT_LOOKUP_CODE.getFieldName());
        this.quantity = rawJsonObject.optInt(TransactionEntryFieldName.QUANTITY.getFieldName());
        this.price = rawJsonObject.optDouble(TransactionEntryFieldName.PRICE.getFieldName());

        value = rawJsonObject.optString(TransactionEntryFieldName.TRANSACTION_REFERENCE_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.transactionReferenceId = UUID.fromString(value);
        }

        value = rawJsonObject.optString(TransactionEntryFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return this;
    }

    @Override
    public JSONObject convertToJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(TransactionEntryFieldName.RECORD_ID.getFieldName(), this.recordId.toString());
            jsonObject.put(TransactionEntryFieldName.PRODUCT_LOOKUP_CODE.getFieldName(), this.productLookupCode);
            jsonObject.put(TransactionEntryFieldName.QUANTITY.getFieldName(), this.quantity);
            jsonObject.put(TransactionEntryFieldName.PRICE.getFieldName(), this.price);
            jsonObject.put(TransactionEntryFieldName.TRANSACTION_REFERENCE_ID.getFieldName(), this.transactionReferenceId);
            jsonObject.put(TransactionEntryFieldName.CREATED_ON.getFieldName(), this.createdOn);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public TransactionEntry() {
        this.recordId = new UUID(0, 0);
        this.createdOn = new Date();
        this.productLookupCode = StringUtils.EMPTY;
        this.price = 0.0;
        this.quantity = 0;
        this.transactionReferenceId =  new UUID(0, 0);
    }

    public TransactionEntry(UUID transactionReferenceId, String productLookupCode, double price){
        this.recordId = new UUID(0, 0);
        this.createdOn = new Date();
        this.transactionReferenceId = transactionReferenceId;
        this.productLookupCode = productLookupCode;
        this.price = price;
        this.quantity = 0;
    }

    public TransactionEntry(TransactionEntryTransition transactionEntryTransition){
        this.recordId = transactionEntryTransition.getRecordId();
        this.createdOn = transactionEntryTransition.getCreatedOn();
        this.productLookupCode = transactionEntryTransition.getLookupCode();
        this.price = transactionEntryTransition.getPrice();
        this.quantity = transactionEntryTransition.getQuantity();
        this.transactionReferenceId = transactionEntryTransition.getTransactionReferenceId();
    }


}

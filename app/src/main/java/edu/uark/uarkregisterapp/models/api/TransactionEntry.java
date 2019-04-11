package edu.uark.uarkregisterapp.models.api;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

import edu.uark.uarkregisterapp.models.api.fields.ProductFieldName;
import edu.uark.uarkregisterapp.models.api.fields.TransactionEntryFieldName;
import edu.uark.uarkregisterapp.models.api.interfaces.ConvertToJsonInterface;
import edu.uark.uarkregisterapp.models.api.interfaces.LoadFromJsonInterface;

public class TransactionEntry implements ConvertToJsonInterface, LoadFromJsonInterface<TransactionEntry> {

    private UUID recordId;
    public UUID getRecordId() {
        return recordId;
    }
    public void setRecordId(UUID recordId) {
        this.recordId = recordId;
    }

    private String productLookupCode;
    public String getLookupCode() {
        return productLookupCode;
    }
    public void setLookupCode(String productLookupCode) {
        this.productLookupCode = productLookupCode;
    }

    private int quantity;
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private double price;
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public JSONObject convertToJson() {
        return null;

    }

    @Override
    public TransactionEntry loadFromJson(JSONObject rawJsonObject) {
        String value = rawJsonObject.optString(TransactionEntryFieldName.RECORD_ID.getFieldName());
        if (!StringUtils.isBlank(value)) {
            this.recordId = UUID.fromString(value);
        }

        this.productLookupCode = rawJsonObject.optString(TransactionEntryFieldName.PRODUCT_LOOKUP_CODE.getFieldName());
        this.quantity = rawJsonObject.optInt(TransactionEntryFieldName.QUANTITY.getFieldName());

        /**value = rawJsonObject.optString(TransactionEntryFieldName.CREATED_ON.getFieldName());
        if (!StringUtils.isBlank(value)) {
            try {
                this.createdOn = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US).parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }**/

        return this;
    }
}

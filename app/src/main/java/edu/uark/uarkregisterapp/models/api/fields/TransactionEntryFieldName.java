package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionEntryFieldName implements FieldNameInterface {

    RECORD_ID("recordid"),
    PRODUCT_LOOKUP_CODE("productlookupcode"),
    QUANTITY("quantity"),
    PRICE("price"),
    TRANSACTION_REFERENCE_ID("transactionreferenceid"),
    CREATED_ON("createdOn");

    private String fieldName;
    public String getFieldName() {
        return this.fieldName;
    }


    TransactionEntryFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

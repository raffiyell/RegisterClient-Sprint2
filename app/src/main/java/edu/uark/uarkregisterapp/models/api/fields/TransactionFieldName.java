package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionFieldName implements FieldNameInterface {
    RECORD_ID("id"), //must be the same as the one defined in the database or in the creation of JSON from server
    CASHIER_ID("cashierId"),
    TOTAL_AMOUNT("total"),
    TRANSACTION_TYPE("transactionType"),
    REFERENCE_ID("referenceId"),
    CREATED_ON("createdOn"),
    TOTAL_ITEMS_SOLD("totalItemsSold"); //todo total items sold may not be needed since it is not defined in the instrucitons in slack

    private String fieldName;
    public String getFieldName(){
        return this.fieldName;
    }

    TransactionFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

package edu.uark.uarkregisterapp.models.api.fields;

import edu.uark.uarkregisterapp.models.api.interfaces.FieldNameInterface;

public enum TransactionFieldName implements FieldNameInterface {
    RECORD_ID("recordid"), //must be the same as the one defined in the database or in the creation of JSON from server
    CASHIER_ID("cashierid"),
    TOTAL_AMOUNT("totalamount"),
    TRANSACTION_TYPE("transactiontype"),
    REFERENCE_ID("referenceid"),
    CREATED_ON("createdon"),
    TOTAL_ITEMS_SOLD("totalitemssold"); //todo total items sold may not be needed since it is not defined in the instrucitons in slack

    private String fieldName;
    public String getFieldName(){
        return this.fieldName;
    }

    TransactionFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

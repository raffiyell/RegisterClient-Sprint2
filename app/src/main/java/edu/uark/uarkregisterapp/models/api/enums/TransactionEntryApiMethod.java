package edu.uark.uarkregisterapp.models.api.enums;

import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public enum TransactionEntryApiMethod implements PathElementInterface {
    NONE(""),
    ;

    @Override
    public String getPathValue() {
        return value;
    }

    private String value;

    TransactionEntryApiMethod(String value) {
        this.value = value;
    }
}

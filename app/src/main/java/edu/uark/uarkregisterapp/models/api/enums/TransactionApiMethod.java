package edu.uark.uarkregisterapp.models.api.enums;

import edu.uark.uarkregisterapp.models.api.interfaces.PathElementInterface;

public enum TransactionApiMethod implements PathElementInterface {
    NONE(""),
  ;

    @Override
    public String getPathValue() {
        return value;
    }

    private String value;

    TransactionApiMethod(String value) {
        this.value = value;
    }
}

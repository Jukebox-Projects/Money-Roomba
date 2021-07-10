package com.moneyroomba.domain.enumeration;

/**
 * The MovementType enumeration.
 */
public enum MovementType {
    INCOME("Ingreso"),
    EXPENSE("Egreso");

    private final String value;

    MovementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

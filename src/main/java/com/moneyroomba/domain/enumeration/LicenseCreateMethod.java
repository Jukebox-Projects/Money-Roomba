package com.moneyroomba.domain.enumeration;

/**
 * The LicenseCreateMethod enumeration.
 */
public enum LicenseCreateMethod {
    MANUAL("Manual"),
    BULK("En bloque");

    private final String value;

    LicenseCreateMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

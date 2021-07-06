package com.moneyroomba.domain.enumeration;

/**
 * The RecurringType enumeration.
 */
public enum RecurringType {
    DAILY("Diariamente"),
    WEEKLY("Semanalmente"),
    MONTHLY("Mensualmente"),
    YEARLY("Anualmente");

    private final String value;

    RecurringType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

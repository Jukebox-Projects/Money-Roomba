package com.moneyroomba.domain.enumeration;

/**
 * The TransactionState enumeration.
 */
public enum TransactionState {
    ACCEPTED("Accepted"),
    DENIED("Denied"),
    PENDING,
    APPROVAL("Pending approval"),
    NA("Does not apply");

    private String value;

    TransactionState() {}

    TransactionState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

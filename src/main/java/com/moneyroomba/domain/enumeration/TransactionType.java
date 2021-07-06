package com.moneyroomba.domain.enumeration;

/**
 * The TransactionType enumeration.
 */
public enum TransactionType {
    MANUAL("Creada manualmente"),
    SCHEDULED("Programada"),
    EMAIL("Recibida por correo"),
    API("Recibida por API"),
    SHARED("Recibida de otro usuario");

    private final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

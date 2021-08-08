package com.moneyroomba.domain.enumeration;

/**
 * The SourceEntity enumeration.
 */
public enum SourceEntity {
    TRANSACTION("Transacción"),
    CONTACT("Contacto favorito"),
    SCHEDULEDTRANSACTION("Transacción programada"),
    LICENSE("Licencia"),
    CATEGORY("Categoría"),
    WALLET("Cartera"),
    USER("Usuario");

    private final String value;

    SourceEntity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

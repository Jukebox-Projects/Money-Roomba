package com.moneyroomba.domain.enumeration;

/**
 * The EventType enumeration.
 */
public enum EventType {
    TRANSCTION_RECEIVED("Transacción entrante"),
    TRANSACTION_SENT("Transacción enviada"),
    REPORT_IS_READY("Reporte listo"),
    LICENSE_PURCHASED("Licencia comprada"),
    LICENSE_GIFTED("Licencia regalada"),
    ADDED_AS_FAVORITE("Añadido como contacto favorito"),
    POSSIBLE_TRANSACTION_ADDED_EMAIL("Transacción potencial añadida"),
    INVALID_ATTACHMENT("Archivo adjunto inválido"),
    IMPORT_LIMIT_REACHED("Se alcanzó el límite de transacciones importadas"),
    CREATE("Creación"),
    UPDATE("Actualización"),
    DELETE("Eliminación"),
    GET("Recuperar");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

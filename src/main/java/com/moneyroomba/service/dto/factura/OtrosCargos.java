package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtrosCargos {

    @JacksonXmlProperty(localName = "TipoDocumento")
    private String tipoDocumento;

    @JacksonXmlProperty(localName = "NumeroIdentidadTercero")
    private String numeroIdentidadTercero;

    @JacksonXmlProperty(localName = "NombreTercero")
    private String nombreTercero;

    @JacksonXmlProperty(localName = "Detalle")
    private String detalle;

    @JacksonXmlProperty(localName = "Porcentaje")
    private double porcentaje;

    @JacksonXmlProperty(localName = "MontoCargo")
    private double montoCargo;
}

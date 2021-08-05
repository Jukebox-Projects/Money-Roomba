package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Impuesto {

    @JacksonXmlProperty(localName = "Codigo")
    private String codigo;

    @JacksonXmlProperty(localName = "CodigoTarifa")
    private String codigoTarifa;

    @JacksonXmlProperty(localName = "Tarifa")
    private double tarifa;

    @JacksonXmlProperty(localName = "FactorIVA")
    private double factorIVA;

    @JacksonXmlProperty(localName = "Monto")
    private double monto;

    @JacksonXmlProperty(localName = "Exoneracion")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Exoneracion exoneracion;
}

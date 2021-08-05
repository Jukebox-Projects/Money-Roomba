package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Descuento {

    @JacksonXmlProperty(localName = "MontoDescuento")
    private double montoDescuento;

    @JacksonXmlProperty(localName = "NaturalezaDescuento")
    private String naturalezaDescuento;
}

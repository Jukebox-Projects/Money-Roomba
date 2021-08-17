package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identificacion {

    @JacksonXmlProperty(localName = "Tipo")
    private String tipo;

    @JacksonXmlProperty(localName = "Numero")
    private String numero;
}

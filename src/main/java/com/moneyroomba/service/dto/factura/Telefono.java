package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Telefono {

    @JacksonXmlProperty(localName = "CodigoPais")
    private int codigoPais;

    @JacksonXmlProperty(localName = "NumTelefono")
    private String numTelefono;
}

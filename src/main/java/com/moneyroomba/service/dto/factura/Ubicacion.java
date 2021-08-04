package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ubicacion {

    @JacksonXmlProperty(localName = "Provincia")
    private int provincia;

    @JacksonXmlProperty(localName = "Canton")
    private int canton;

    @JacksonXmlProperty(localName = "Distrito")
    private int distrito;

    @JacksonXmlProperty(localName = "Barrio")
    private int barrio;

    @JacksonXmlProperty(localName = "OtrasSenas")
    private String otrasSenas;
}

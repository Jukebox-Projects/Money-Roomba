package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Otros {

    @JacksonXmlProperty(localName = "OtroTexto")
    @JacksonXmlElementWrapper(useWrapping = false)
    private OtroTexto otroTexto;

    @JacksonXmlProperty(localName = "OtroContenido")
    @JacksonXmlElementWrapper(useWrapping = false)
    private OtroContenido otroContenido;
}

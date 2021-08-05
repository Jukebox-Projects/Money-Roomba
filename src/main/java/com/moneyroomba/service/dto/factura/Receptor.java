package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Receptor {

    @JacksonXmlProperty(localName = "Nombre")
    private String nombre;

    @JacksonXmlProperty(localName = "NombreComercial")
    private String nombreComercial;

    @JacksonXmlProperty(localName = "IdentificacionExtranjero")
    private String identificacionExtranjero;

    @JacksonXmlProperty(localName = "Identificacion")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Identificacion identificacion;

    @JacksonXmlProperty(localName = "OtrasSenasExtranjero")
    private String otrasSenasExtranjero;

    @JacksonXmlProperty(localName = "Ubicacion")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Ubicacion ubicacion;

    @JacksonXmlProperty(localName = "Telefono")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Telefono telefono;

    @JacksonXmlProperty(localName = "Fax")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Fax fax;

    @JacksonXmlProperty(localName = "CorreoElectronico")
    private String correoElectronico;
}

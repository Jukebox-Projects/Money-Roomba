package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Emisor {

    @JacksonXmlProperty(localName = "Nombre")
    private String nombre;

    @JacksonXmlProperty(localName = "NombreComercial")
    private String nombreComercial;

    @JacksonXmlProperty(localName = "Identificacion")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Identificacion identificacion;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}

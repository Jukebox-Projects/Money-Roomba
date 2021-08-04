package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Exoneracion {

    //TipoDocumento NumeroDocumento NombreInstitucion FechaEmision PorcentajeExoneracion MontoExoneracion

    @JacksonXmlProperty(localName = "TipoDocumento")
    private String tipoDocumento;

    @JacksonXmlProperty(localName = "NumeroDocumento")
    private String numeroDocumento;

    @JacksonXmlProperty(localName = "NombreInstitucion")
    private String nombreInstitucion;

    @JacksonXmlProperty(localName = "FechaEmision")
    private Date fechaEmision;

    @JacksonXmlProperty(localName = "PorcentajeExoneracion")
    private int porcentajeExoneracion;

    @JacksonXmlProperty(localName = "MontoExoneracion")
    private double montoExoneracion;
}

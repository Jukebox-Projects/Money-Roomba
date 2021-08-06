package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InformacionReferencia {

    @JacksonXmlProperty(localName = "TipoDoc")
    private String tipoDoc;

    @JacksonXmlProperty(localName = "Numero")
    private String numero;

    @JacksonXmlProperty(localName = "FechaEmision")
    private Date fechaEmision;

    @JacksonXmlProperty(localName = "Codigo")
    private String codigo;

    @JacksonXmlProperty(localName = "Razon")
    private String razon;
}

package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleServicio {

    @JacksonXmlProperty(localName = "LineaDetalle")
    @JacksonXmlElementWrapper(useWrapping = false)
    private LineaDetalle[] lineasDetalles;

    public LineaDetalle[] getLineasDetalles() {
        return lineasDetalles;
    }

    public void setLineasDetalles(LineaDetalle[] lineasDetalles) {
        this.lineasDetalles = lineasDetalles;
    }
}

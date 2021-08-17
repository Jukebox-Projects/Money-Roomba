package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumenFactura {

    @JacksonXmlProperty(localName = "CodigoTipoMoneda")
    @JacksonXmlElementWrapper(useWrapping = false)
    private CodigoTipoMoneda codigoTipoMoneda;

    @JacksonXmlProperty(localName = "TotalServGravados")
    private double totalServGravados;

    @JacksonXmlProperty(localName = "TotalServExentos")
    private double totalServExentos;

    @JacksonXmlProperty(localName = "TotalServExonerado")
    private double totalServExonerado;

    @JacksonXmlProperty(localName = "TotalMercanciasGravadas")
    private double totalMercanciasGravadas;

    @JacksonXmlProperty(localName = "TotalMercanciasExentas")
    private double totalMercanciasExentas;

    @JacksonXmlProperty(localName = "TotalMercExonerada")
    private double totalMercExonerada;

    @JacksonXmlProperty(localName = "TotalGravado")
    private double totalGravado;

    @JacksonXmlProperty(localName = "TotalExento")
    private double totalExento;

    @JacksonXmlProperty(localName = "TotalExonerado")
    private double totalExonerado;

    @JacksonXmlProperty(localName = "TotalVenta")
    private double totalVenta;

    @JacksonXmlProperty(localName = "TotalDescuentos")
    private double totalDescuentos;

    @JacksonXmlProperty(localName = "TotalVentaNeta")
    private double totalVentaNeta;

    @JacksonXmlProperty(localName = "TotalImpuesto")
    private double totalImpuesto;

    @JacksonXmlProperty(localName = "TotalIVADevuelto")
    private double totalIVADevuelto;

    @JacksonXmlProperty(localName = "TotalOtrosCargos")
    private double totalOtrosCargos;

    @JacksonXmlProperty(localName = "TotalComprobante")
    private double totalComprobante;

    public CodigoTipoMoneda getCodigoTipoMoneda() {
        return codigoTipoMoneda;
    }

    public void setCodigoTipoMoneda(CodigoTipoMoneda codigoTipoMoneda) {
        this.codigoTipoMoneda = codigoTipoMoneda;
    }

    public double getTotalComprobante() {
        return totalComprobante;
    }

    public void setTotalComprobante(double totalComprobante) {
        this.totalComprobante = totalComprobante;
    }
}

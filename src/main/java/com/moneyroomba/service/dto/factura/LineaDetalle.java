package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineaDetalle {

    @JacksonXmlProperty(localName = "NumeroLinea")
    private int numeroLinea;

    @JacksonXmlProperty(localName = "Codigo")
    private String codigo;

    @JacksonXmlProperty(localName = "CodigoComercial")
    @JacksonXmlElementWrapper(useWrapping = false)
    private CodigoComercial codigoComercial;

    @JacksonXmlProperty(localName = "Cantidad")
    private double cantidad;

    @JacksonXmlProperty(localName = "UnidadMedida")
    private String UnidadMedida;

    @JacksonXmlProperty(localName = "UnidadMedidaComercial")
    private String unidadMedidaComercial;

    @JacksonXmlProperty(localName = "Detalle")
    private String detalle;

    @JacksonXmlProperty(localName = "PrecioUnitario")
    private double precioUnitario;

    @JacksonXmlProperty(localName = "MontoTotal")
    private double montoTotal;

    // Descuento
    @JacksonXmlProperty(localName = "Descuento")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Descuento descuento;

    @JacksonXmlProperty(localName = "SubTotal")
    private double subTotal;

    @JacksonXmlProperty(localName = "BaseImponible")
    private double baseImponible;

    // Impuesto
    @JacksonXmlProperty(localName = "Impuesto")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Impuesto impuesto;

    @JacksonXmlProperty(localName = "ImpuestoNeto")
    private double impuestoNeto;

    @JacksonXmlProperty(localName = "MontoTotalLinea")
    private double montoTotalLinea;

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CodigoComercial getCodigoComercial() {
        return codigoComercial;
    }

    public void setCodigoComercial(CodigoComercial codigoComercial) {
        this.codigoComercial = codigoComercial;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidadMedida() {
        return UnidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        UnidadMedida = unidadMedida;
    }

    public String getUnidadMedidaComercial() {
        return unidadMedidaComercial;
    }

    public void setUnidadMedidaComercial(String unidadMedidaComercial) {
        this.unidadMedidaComercial = unidadMedidaComercial;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(double baseImponible) {
        this.baseImponible = baseImponible;
    }

    public Impuesto getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Impuesto impuesto) {
        this.impuesto = impuesto;
    }

    public double getImpuestoNeto() {
        return impuestoNeto;
    }

    public void setImpuestoNeto(double impuestoNeto) {
        this.impuestoNeto = impuestoNeto;
    }

    public double getMontoTotalLinea() {
        return montoTotalLinea;
    }

    public void setMontoTotalLinea(double montoTotalLinea) {
        this.montoTotalLinea = montoTotalLinea;
    }
}

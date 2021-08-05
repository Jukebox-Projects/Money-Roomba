package com.moneyroomba.service.dto.factura;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.Date;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TiqueteElectronico {

    @JacksonXmlProperty(localName = "Clave")
    private String clave;

    @JacksonXmlProperty(localName = "CodigoActividad")
    private String codigoActividad;

    @JacksonXmlProperty(localName = "NumeroConsecutivo")
    private String numeroConsecutivo;

    @JacksonXmlProperty(localName = "FechaEmision")
    private Date fechaEmision;

    @JacksonXmlProperty(localName = "Emisor")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Emisor emisor;

    @JacksonXmlProperty(localName = "Receptor")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Receptor receptor;

    @JacksonXmlProperty(localName = "CondicionVenta")
    private String condicionVenta;

    @JacksonXmlProperty(localName = "PlazoCredito")
    private String plazoCredito;

    @JacksonXmlProperty(localName = "MedioPago")
    private String medioPago;

    @JacksonXmlProperty(localName = "DetalleServicio")
    @JacksonXmlElementWrapper(useWrapping = false)
    private DetalleServicio detalleServicio;

    @JacksonXmlProperty(localName = "OtrosCargos")
    @JacksonXmlElementWrapper(useWrapping = false)
    private OtrosCargos otrosCargos;

    @JacksonXmlProperty(localName = "ResumenFactura")
    @JacksonXmlElementWrapper(useWrapping = false)
    private ResumenFactura resumenFactura;

    /*
	@JacksonXmlProperty(localName = "InformacionReferencia")
	@JacksonXmlElementWrapper(useWrapping = false)
	private InformacionReferencia informacionReferencia;
	@JacksonXmlProperty(localName = "Otros")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Otros otros;*/

    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public String getNumeroConsecutivo() {
        return numeroConsecutivo;
    }

    public void setNumeroConsecutivo(String numeroConsecutivo) {
        this.numeroConsecutivo = numeroConsecutivo;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public DetalleServicio getDetalleServicio() {
        return detalleServicio;
    }

    public void setDetalleServicio(DetalleServicio detalleServicio) {
        this.detalleServicio = detalleServicio;
    }

    public ResumenFactura getResumenFactura() {
        return resumenFactura;
    }

    public void setResumenFactura(ResumenFactura resumenFactura) {
        this.resumenFactura = resumenFactura;
    }
}

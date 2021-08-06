package com.moneyroomba.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.moneyroomba.repository.CurrencyRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.repository.WalletRepository;
import com.moneyroomba.service.dto.factura.*;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InvoiceXMLService {

    private final Logger log = LoggerFactory.getLogger(InvoiceXMLService.class);

    private final CurrencyRepository currencyRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final TransactionService transactionService;

    public InvoiceXMLService(
        CurrencyRepository currencyRepository,
        WalletRepository walletRepository,
        UserRepository userRepository,
        UserDetailsRepository userDetailsRepository,
        TransactionService transactionService
    ) {
        this.currencyRepository = currencyRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.transactionService = transactionService;
    }

    public TiqueteElectronico save(String xml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();

            TiqueteElectronico deserializedData = xmlMapper.readValue(xml, TiqueteElectronico.class);

            if (deserializedData.getClave() == null || deserializedData.getClave().isEmpty()) return null;
            if (deserializedData.getNumeroConsecutivo() == null || deserializedData.getNumeroConsecutivo().isEmpty()) return null;

            if (deserializedData.getEmisor() == null) {
                Emisor emisor = new Emisor();
                emisor.setNombre("");
                emisor.setNombreComercial("");
                deserializedData.setEmisor(emisor);
            }

            if (deserializedData.getEmisor().getNombre() == null) {
                deserializedData.getEmisor().setNombre("");
            }
            if (deserializedData.getEmisor().getNombreComercial() == null) {
                deserializedData.getEmisor().setNombreComercial("");
            }

            if (
                deserializedData.getFechaEmision() == null ||
                deserializedData.getFechaEmision().toInstant() == null ||
                deserializedData.getFechaEmision().toInstant().atZone(ZoneId.systemDefault()) == null ||
                deserializedData.getFechaEmision().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == null
            ) {
                deserializedData.setFechaEmision(new Date());
            }

            if (deserializedData.getResumenFactura() == null) {
                ResumenFactura resumenFactura = new ResumenFactura();
                resumenFactura.setTotalComprobante(0.0);
                deserializedData.setResumenFactura(resumenFactura);
                if (deserializedData.getResumenFactura().getCodigoTipoMoneda() == null) {
                    CodigoTipoMoneda codigoTipoMoneda = new CodigoTipoMoneda();
                    codigoTipoMoneda.setCodigoMoneda("USD");
                    codigoTipoMoneda.setTipoCambio(1.0);
                    resumenFactura.setCodigoTipoMoneda(codigoTipoMoneda);
                }
            }

            if (deserializedData.getDetalleServicio() == null) {
                DetalleServicio detalleServicio = new DetalleServicio();
                deserializedData.setDetalleServicio(detalleServicio);
                if (
                    deserializedData.getDetalleServicio().getLineasDetalles() == null ||
                    deserializedData.getDetalleServicio().getLineasDetalles().length == 0
                ) {
                    LineaDetalle[] lineaDetalles = new LineaDetalle[0];
                    detalleServicio.setLineasDetalles(lineaDetalles);
                }
            }

            //log.debug("Tiquete electrónico : {}", deserializedData.toString());
            return deserializedData;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            // handle the exception
            return null;
        }
    }
}

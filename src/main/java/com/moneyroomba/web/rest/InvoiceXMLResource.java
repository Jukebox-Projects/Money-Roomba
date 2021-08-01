package com.moneyroomba.web.rest;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.moneyroomba.domain.Transaction;
import com.moneyroomba.service.InvoiceXMLService;
import com.moneyroomba.service.TransactionService;
import com.moneyroomba.service.dto.factura.TiqueteElectronico;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/invoicexml")
@Transactional
public class InvoiceXMLResource {

    private final Logger log = LoggerFactory.getLogger(InvoiceXMLResource.class);

    private static final String ENTITY_NAME = "invoice_xml";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;
    private final InvoiceXMLService invoiceXMLService;

    public InvoiceXMLResource(TransactionService transactionService, InvoiceXMLService invoiceXMLService) {
        this.transactionService = transactionService;
        this.invoiceXMLService = invoiceXMLService;
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public ResponseEntity<Transaction> singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            throw new BadRequestAlertException("No file was selected", ENTITY_NAME, "file.noFileSelected");
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String contenido = new String(file.getBytes());

            //ve file : IMG_2331.JPG
            //2021-07-30 10:01:59.569 DEBUG 254248 --- [  XNIO-1 task-1] c.m.web.rest.InvoiceXMLResource          : REST request to save file : file
            //2021-07-30 10:01:59.569 DEBUG 254248 --- [  XNIO-1 task-1] c.m.web.rest.InvoiceXMLResource          : REST request to save file : image/jpeg
            log.debug("REST request to save file : {}", file.getOriginalFilename());
            log.debug("REST request to save file : {}", file.getName());
            log.debug("REST request to save file : {}", file.getContentType());
            log.debug("REST request to save file : {}", contenido);

            if (
                file.getContentType().equals("text/xml") &&
                file
                    .getOriginalFilename()
                    .substring(file.getOriginalFilename().length() - 3, file.getOriginalFilename().length())
                    .equalsIgnoreCase("xml")
            ) {
                TiqueteElectronico deserializedData = invoiceXMLService.save(contenido);
                if (deserializedData == null) throw new BadRequestAlertException(
                    "Could not extract file",
                    ENTITY_NAME,
                    "file.could.not.extract"
                );

                Transaction transaction = transactionService.saveXML(deserializedData);

                return ResponseEntity
                    .created(new URI("/api/transactions/" + transaction.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transaction.getId().toString()))
                    .body(transaction);
            } else {
                throw new BadRequestAlertException("File type not supported, File type must be XML", ENTITY_NAME, "file.wrongFileType");
            }
        } catch (IOException | URISyntaxException e) {
            throw new BadRequestAlertException("Could not extract file", ENTITY_NAME, "file.could.not.extract");
        }
    }
}

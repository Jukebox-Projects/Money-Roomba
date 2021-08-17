package com.moneyroomba.web.rest;

import ch.qos.logback.classic.joran.action.ContextNameAction;
import com.moneyroomba.service.ContactService;
import com.moneyroomba.service.dto.ContactDTO;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class ContactResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "contact";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private ContactService contactService;

    public ContactResource(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping("/contacts")
    public ResponseEntity<?> createContact(@RequestBody String contactIdentifier) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", contactIdentifier);

        boolean result = contactService.save(contactIdentifier);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactDTO>> getAllContacts() {
        log.debug("REST request to get contacts by user: {}");
        List<ContactDTO> entityList = contactService.findAll();
        return ResponseEntity.ok().body(entityList);
    }

    @DeleteMapping("/contacts/{contactIdentifier}")
    public ResponseEntity<Void> deleteWallet(@PathVariable String contactIdentifier) {
        log.debug("REST request to delete contact : {}", contactIdentifier);
        contactService.delete(contactIdentifier);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, contactIdentifier))
            .build();
    }
}

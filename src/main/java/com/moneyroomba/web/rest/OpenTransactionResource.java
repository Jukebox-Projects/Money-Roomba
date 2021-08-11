package com.moneyroomba.web.rest;

import com.moneyroomba.domain.Transaction;
import com.moneyroomba.domain.User;
import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.domain.enumeration.TransactionState;
import com.moneyroomba.domain.enumeration.TransactionType;
import com.moneyroomba.repository.TransactionRepository;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.repository.UserRepository;
import com.moneyroomba.security.AuthoritiesConstants;
import com.moneyroomba.security.SecurityUtils;
import com.moneyroomba.service.TransactionQueryService;
import com.moneyroomba.service.TransactionService;
import com.moneyroomba.service.criteria.TransactionCriteria;
import com.moneyroomba.service.exception.NoSuchElementFoundException;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Transaction}.
 */
@RestController
@RequestMapping("/api")
public class OpenTransactionResource {

    private final Logger log = LoggerFactory.getLogger(OpenTransactionResource.class);

    private static final String ENTITY_NAME = "transaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionService transactionService;
    private final UserDetailsRepository userDetailsRepository;

    public OpenTransactionResource(TransactionService transactionService, UserDetailsRepository userDetailsRepository) {
        this.transactionService = transactionService;
        this.userDetailsRepository = userDetailsRepository;
    }

    @PostMapping("/opentransaction")
    public ResponseEntity<JsonResponse> createOpenTransaction(
        @RequestHeader(value = "API-Key") String apikey,
        @Valid @RequestBody Transaction transaction
    ) throws URISyntaxException {
        log.debug("REST request to save Transaction : {}", transaction);
        Optional<UserDetails> userDetails = userDetailsRepository.findOneByApiKey(apikey);
        if (userDetails.isPresent()) {
            Transaction result;
            if (transaction.getId() != null) {
                return new ResponseEntity<JsonResponse>(
                    new JsonResponse("A new transaction cannot already have an ID"),
                    HttpStatus.BAD_REQUEST
                );
            }

            transaction.setSourceUser(userDetails.get());
            transaction.setState(TransactionState.PENDING_APPROVAL);
            transaction.setTransactionType(TransactionType.API);

            result = transactionService.create(transaction);
            if (result != null) {
                return new ResponseEntity<JsonResponse>(new JsonResponse("Transaction created"), HttpStatus.OK);
            } else {
                return new ResponseEntity<JsonResponse>(new JsonResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<JsonResponse>(new JsonResponse("APIKEY not found"), HttpStatus.BAD_REQUEST);
        }
    }

    private class JsonResponse {

        private String message;

        public JsonResponse() {}

        public JsonResponse(String message) {
            super();
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

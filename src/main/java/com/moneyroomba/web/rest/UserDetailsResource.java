package com.moneyroomba.web.rest;

import com.moneyroomba.domain.UserDetails;
import com.moneyroomba.repository.UserDetailsRepository;
import com.moneyroomba.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.moneyroomba.domain.UserDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserDetailsResource {

    private final Logger log = LoggerFactory.getLogger(UserDetailsResource.class);

    private static final String ENTITY_NAME = "userDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsResource(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    /**
     * {@code POST  /user-details} : Create a new userDetails.
     *
     * @param userDetails the userDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDetails, or with status {@code 400 (Bad Request)} if the userDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-details")
    public ResponseEntity<UserDetails> createUserDetails(@Valid @RequestBody UserDetails userDetails) throws URISyntaxException {
        log.debug("REST request to save UserDetails : {}", userDetails);
        if (userDetails.getId() != null) {
            throw new BadRequestAlertException("A new userDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDetails result = userDetailsRepository.save(userDetails);
        return ResponseEntity
            .created(new URI("/api/user-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-details/:id} : Updates an existing userDetails.
     *
     * @param id the id of the userDetails to save.
     * @param userDetails the userDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetails,
     * or with status {@code 400 (Bad Request)} if the userDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-details/{id}")
    public ResponseEntity<UserDetails> updateUserDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserDetails userDetails
    ) throws URISyntaxException {
        log.debug("REST request to update UserDetails : {}, {}", id, userDetails);
        if (userDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserDetails result = userDetailsRepository.save(userDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-details/:id} : Partial updates given fields of an existing userDetails, field will ignore if it is null
     *
     * @param id the id of the userDetails to save.
     * @param userDetails the userDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetails,
     * or with status {@code 400 (Bad Request)} if the userDetails is not valid,
     * or with status {@code 404 (Not Found)} if the userDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserDetails> partialUpdateUserDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserDetails userDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserDetails partially : {}, {}", id, userDetails);
        if (userDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDetails> result = userDetailsRepository
            .findById(userDetails.getId())
            .map(
                existingUserDetails -> {
                    if (userDetails.getCountry() != null) {
                        existingUserDetails.setCountry(userDetails.getCountry());
                    }
                    if (userDetails.getPhone() != null) {
                        existingUserDetails.setPhone(userDetails.getPhone());
                    }
                    if (userDetails.getApiKey() != null) {
                        existingUserDetails.setApiKey(userDetails.getApiKey());
                    }
                    if (userDetails.getNotifications() != null) {
                        existingUserDetails.setNotifications(userDetails.getNotifications());
                    }
                    if (userDetails.getIsActive() != null) {
                        existingUserDetails.setIsActive(userDetails.getIsActive());
                    }
                    if (userDetails.getIsTemporaryPassword() != null) {
                        existingUserDetails.setIsTemporaryPassword(userDetails.getIsTemporaryPassword());
                    }

                    return existingUserDetails;
                }
            )
            .map(userDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /user-details} : get all the userDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDetails in body.
     */
    @GetMapping("/user-details")
    public List<UserDetails> getAllUserDetails(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UserDetails");
        return userDetailsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /user-details/:id} : get the "id" userDetails.
     *
     * @param id the id of the userDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-details/{id}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable Long id) {
        log.debug("REST request to get UserDetails : {}", id);
        Optional<UserDetails> userDetails = userDetailsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(userDetails);
    }

    /**
     * {@code DELETE  /user-details/:id} : delete the "id" userDetails.
     *
     * @param id the id of the userDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-details/{id}")
    public ResponseEntity<Void> deleteUserDetails(@PathVariable Long id) {
        log.debug("REST request to delete UserDetails : {}", id);
        userDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

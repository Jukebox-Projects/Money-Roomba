package com.moneyroomba.service;

import com.moneyroomba.domain.License;
import com.moneyroomba.repository.LicenseRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link License}.
 */
@Service
@Transactional
public class LicenseService {

    private final Logger log = LoggerFactory.getLogger(LicenseService.class);

    private final LicenseRepository licenseRepository;

    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    /**
     * Save a license.
     *
     * @param license the entity to save.
     * @return the persisted entity.
     */
    public License save(License license) {
        log.debug("Request to save License : {}", license);
        return licenseRepository.save(license);
    }

    /**
     * Partially update a license.
     *
     * @param license the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<License> partialUpdate(License license) {
        log.debug("Request to partially update License : {}", license);

        return licenseRepository
            .findById(license.getId())
            .map(
                existingLicense -> {
                    if (license.getCode() != null) {
                        existingLicense.setCode(license.getCode());
                    }
                    if (license.getIsAssigned() != null) {
                        existingLicense.setIsAssigned(license.getIsAssigned());
                    }
                    if (license.getIsActive() != null) {
                        existingLicense.setIsActive(license.getIsActive());
                    }
                    if (license.getCreateMethod() != null) {
                        existingLicense.setCreateMethod(license.getCreateMethod());
                    }

                    return existingLicense;
                }
            )
            .map(licenseRepository::save);
    }

    /**
     * Get all the licenses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<License> findAll() {
        log.debug("Request to get all Licenses");
        return licenseRepository.findAll();
    }

    /**
     * Get one license by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<License> findOne(Long id) {
        log.debug("Request to get License : {}", id);
        return licenseRepository.findById(id);
    }

    /**
     * Delete the license by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        Optional<License> license = findOne(id);
        if (license.get() != null) {
            if (!license.get().getIsAssigned()) {
                log.debug("Request to delete License : {}", id);
                licenseRepository.deleteById(id);
            }
        }
    }
}

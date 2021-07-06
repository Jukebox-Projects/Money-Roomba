package com.moneyroomba.service;

import com.moneyroomba.domain.SystemSetting;
import com.moneyroomba.repository.SystemSettingRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SystemSetting}.
 */
@Service
@Transactional
public class SystemSettingService {

    private final Logger log = LoggerFactory.getLogger(SystemSettingService.class);

    private final SystemSettingRepository systemSettingRepository;

    public SystemSettingService(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    /**
     * Save a systemSetting.
     *
     * @param systemSetting the entity to save.
     * @return the persisted entity.
     */
    public SystemSetting save(SystemSetting systemSetting) {
        log.debug("Request to save SystemSetting : {}", systemSetting);
        return systemSettingRepository.save(systemSetting);
    }

    /**
     * Partially update a systemSetting.
     *
     * @param systemSetting the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SystemSetting> partialUpdate(SystemSetting systemSetting) {
        log.debug("Request to partially update SystemSetting : {}", systemSetting);

        return systemSettingRepository
            .findById(systemSetting.getId())
            .map(
                existingSystemSetting -> {
                    if (systemSetting.getKey() != null) {
                        existingSystemSetting.setKey(systemSetting.getKey());
                    }
                    if (systemSetting.getValue() != null) {
                        existingSystemSetting.setValue(systemSetting.getValue());
                    }

                    return existingSystemSetting;
                }
            )
            .map(systemSettingRepository::save);
    }

    /**
     * Get all the systemSettings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SystemSetting> findAll() {
        log.debug("Request to get all SystemSettings");
        return systemSettingRepository.findAll();
    }

    /**
     * Get one systemSetting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemSetting> findOne(Long id) {
        log.debug("Request to get SystemSetting : {}", id);
        return systemSettingRepository.findById(id);
    }

    /**
     * Delete the systemSetting by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SystemSetting : {}", id);
        systemSettingRepository.deleteById(id);
    }
}

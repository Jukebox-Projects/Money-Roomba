package com.moneyroomba.service;

import com.moneyroomba.domain.Icon;
import com.moneyroomba.repository.IconRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Icon}.
 */
@Service
@Transactional
public class IconService {

    private final Logger log = LoggerFactory.getLogger(IconService.class);

    private final IconRepository iconRepository;

    public IconService(IconRepository iconRepository) {
        this.iconRepository = iconRepository;
    }

    /**
     * Save a icon.
     *
     * @param icon the entity to save.
     * @return the persisted entity.
     */
    public Icon save(Icon icon) {
        log.debug("Request to save Icon : {}", icon);
        return iconRepository.save(icon);
    }

    /**
     * Partially update a icon.
     *
     * @param icon the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Icon> partialUpdate(Icon icon) {
        log.debug("Request to partially update Icon : {}", icon);

        return iconRepository
            .findById(icon.getId())
            .map(
                existingIcon -> {
                    if (icon.getUrl() != null) {
                        existingIcon.setUrl(icon.getUrl());
                    }
                    if (icon.getName() != null) {
                        existingIcon.setName(icon.getName());
                    }

                    return existingIcon;
                }
            )
            .map(iconRepository::save);
    }

    /**
     * Get all the icons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Icon> findAll() {
        log.debug("Request to get all Icons");
        return iconRepository.findAll();
    }

    /**
     * Get one icon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Icon> findOne(Long id) {
        log.debug("Request to get Icon : {}", id);
        return iconRepository.findById(id);
    }

    /**
     * Delete the icon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Icon : {}", id);
        iconRepository.deleteById(id);
    }
}

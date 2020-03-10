package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.service.dto.PreferenceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.hexlet.hexletcorrection.domain.Preference}.
 */
public interface PreferenceService {

    /**
     * Save a preference.
     *
     * @param preferenceDTO the entity to save.
     * @return the persisted entity.
     */
    PreferenceDTO save(PreferenceDTO preferenceDTO);

    /**
     * Create a preference from {@link User}.
     *
     * @param user the entity to save.
     * @return the persisted entity.
     */
    PreferenceDTO createFromUser(User user);

    /**
     * Get all the preferences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PreferenceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" preference.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PreferenceDTO> findOne(Long id);

    /**
     * Delete the "id" preference.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

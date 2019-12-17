package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.service.dto.CorrectionDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.hexlet.hexletcorrection.domain.Correction}.
 */
public interface CorrectionService {

    /**
     * Save a correction.
     *
     * @param correctionDTO the entity to save.
     * @return the persisted entity.
     */
    CorrectionDTO save(CorrectionDTO correctionDTO);

    /**
     * Get all the corrections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CorrectionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" correction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CorrectionDTO> findOne(Long id);

    /**
     * Delete the "id" correction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

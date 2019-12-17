package io.hexlet.hexletcorrection.service;

import io.hexlet.hexletcorrection.service.dto.CorrecterDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link io.hexlet.hexletcorrection.domain.Correcter}.
 */
public interface CorrecterService {

    /**
     * Save a correcter.
     *
     * @param correcterDTO the entity to save.
     * @return the persisted entity.
     */
    CorrecterDTO save(CorrecterDTO correcterDTO);

    /**
     * Get all the correcters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CorrecterDTO> findAll(Pageable pageable);


    /**
     * Get the "id" correcter.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CorrecterDTO> findOne(Long id);

    /**
     * Delete the "id" correcter.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

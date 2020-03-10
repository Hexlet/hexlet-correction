package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.repository.CorrectionRepository;
import io.hexlet.hexletcorrection.service.CorrectionService;
import io.hexlet.hexletcorrection.service.dto.CorrectionDTO;
import io.hexlet.hexletcorrection.service.mapper.CorrectionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Correction}.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CorrectionServiceImpl implements CorrectionService {

    private final CorrectionRepository correctionRepository;

    private final CorrectionMapper correctionMapper;

    /**
     * Save a correction.
     *
     * @param correctionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CorrectionDTO save(CorrectionDTO correctionDTO) {
        log.debug("Request to save Correction : {}", correctionDTO);
        Correction correction = correctionMapper.toEntity(correctionDTO);
        correction = correctionRepository.save(correction);
        return correctionMapper.toDto(correction);
    }

    /**
     * Get all the corrections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorrectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Corrections");
        return correctionRepository.findAll(pageable)
            .map(correctionMapper::toDto);
    }

    /**
     * Get one correction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CorrectionDTO> findOne(Long id) {
        log.debug("Request to get Correction : {}", id);
        return correctionRepository.findById(id)
            .map(correctionMapper::toDto);
    }

    /**
     * Delete the correction by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Correction : {}", id);
        correctionRepository.deleteById(id);
    }
}

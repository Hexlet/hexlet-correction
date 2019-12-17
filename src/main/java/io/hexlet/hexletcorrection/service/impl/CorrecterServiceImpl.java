package io.hexlet.hexletcorrection.service.impl;

import io.hexlet.hexletcorrection.service.CorrecterService;
import io.hexlet.hexletcorrection.domain.Correcter;
import io.hexlet.hexletcorrection.repository.CorrecterRepository;
import io.hexlet.hexletcorrection.service.dto.CorrecterDTO;
import io.hexlet.hexletcorrection.service.mapper.CorrecterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Correcter}.
 */
@Service
@Transactional
public class CorrecterServiceImpl implements CorrecterService {

    private final Logger log = LoggerFactory.getLogger(CorrecterServiceImpl.class);

    private final CorrecterRepository correcterRepository;

    private final CorrecterMapper correcterMapper;

    public CorrecterServiceImpl(CorrecterRepository correcterRepository, CorrecterMapper correcterMapper) {
        this.correcterRepository = correcterRepository;
        this.correcterMapper = correcterMapper;
    }

    /**
     * Save a correcter.
     *
     * @param correcterDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CorrecterDTO save(CorrecterDTO correcterDTO) {
        log.debug("Request to save Correcter : {}", correcterDTO);
        Correcter correcter = correcterMapper.toEntity(correcterDTO);
        correcter = correcterRepository.save(correcter);
        return correcterMapper.toDto(correcter);
    }

    /**
     * Get all the correcters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CorrecterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Correcters");
        return correcterRepository.findAll(pageable)
            .map(correcterMapper::toDto);
    }


    /**
     * Get one correcter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CorrecterDTO> findOne(Long id) {
        log.debug("Request to get Correcter : {}", id);
        return correcterRepository.findById(id)
            .map(correcterMapper::toDto);
    }

    /**
     * Delete the correcter by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Correcter : {}", id);
        correcterRepository.deleteById(id);
    }
}

package io.hexlet.hexletcorrection.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.hexlet.hexletcorrection.domain.Correcter;
import io.hexlet.hexletcorrection.domain.*; // for static metamodels
import io.hexlet.hexletcorrection.repository.CorrecterRepository;
import io.hexlet.hexletcorrection.service.dto.CorrecterCriteria;
import io.hexlet.hexletcorrection.service.dto.CorrecterDTO;
import io.hexlet.hexletcorrection.service.mapper.CorrecterMapper;

/**
 * Service for executing complex queries for {@link Correcter} entities in the database.
 * The main input is a {@link CorrecterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorrecterDTO} or a {@link Page} of {@link CorrecterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorrecterQueryService extends QueryService<Correcter> {

    private final Logger log = LoggerFactory.getLogger(CorrecterQueryService.class);

    private final CorrecterRepository correcterRepository;

    private final CorrecterMapper correcterMapper;

    public CorrecterQueryService(CorrecterRepository correcterRepository, CorrecterMapper correcterMapper) {
        this.correcterRepository = correcterRepository;
        this.correcterMapper = correcterMapper;
    }

    /**
     * Return a {@link List} of {@link CorrecterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorrecterDTO> findByCriteria(CorrecterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Correcter> specification = createSpecification(criteria);
        return correcterMapper.toDto(correcterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorrecterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorrecterDTO> findByCriteria(CorrecterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Correcter> specification = createSpecification(criteria);
        return correcterRepository.findAll(specification, page)
            .map(correcterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorrecterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Correcter> specification = createSpecification(criteria);
        return correcterRepository.count(specification);
    }

    /**
     * Function to convert {@link CorrecterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Correcter> createSpecification(CorrecterCriteria criteria) {
        Specification<Correcter> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Correcter_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Correcter_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Correcter_.lastName));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Correcter_.status));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Correcter_.email));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Correcter_.password));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Correcter_.phone));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Correcter_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCorrectionsInProgressId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrectionsInProgressId(),
                    root -> root.join(Correcter_.correctionsInProgresses, JoinType.LEFT).get(Correction_.id)));
            }
            if (criteria.getCorrectionsResolvedId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrectionsResolvedId(),
                    root -> root.join(Correcter_.correctionsResolveds, JoinType.LEFT).get(Correction_.id)));
            }
            if (criteria.getCommentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommentsId(),
                    root -> root.join(Correcter_.comments, JoinType.LEFT).get(Comment_.id)));
            }
        }
        return specification;
    }
}

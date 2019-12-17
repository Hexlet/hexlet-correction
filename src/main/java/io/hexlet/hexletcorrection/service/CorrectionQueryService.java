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

import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.domain.*; // for static metamodels
import io.hexlet.hexletcorrection.repository.CorrectionRepository;
import io.hexlet.hexletcorrection.service.dto.CorrectionCriteria;
import io.hexlet.hexletcorrection.service.dto.CorrectionDTO;
import io.hexlet.hexletcorrection.service.mapper.CorrectionMapper;

/**
 * Service for executing complex queries for {@link Correction} entities in the database.
 * The main input is a {@link CorrectionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CorrectionDTO} or a {@link Page} of {@link CorrectionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CorrectionQueryService extends QueryService<Correction> {

    private final Logger log = LoggerFactory.getLogger(CorrectionQueryService.class);

    private final CorrectionRepository correctionRepository;

    private final CorrectionMapper correctionMapper;

    public CorrectionQueryService(CorrectionRepository correctionRepository, CorrectionMapper correctionMapper) {
        this.correctionRepository = correctionRepository;
        this.correctionMapper = correctionMapper;
    }

    /**
     * Return a {@link List} of {@link CorrectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CorrectionDTO> findByCriteria(CorrectionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Correction> specification = createSpecification(criteria);
        return correctionMapper.toDto(correctionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CorrectionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CorrectionDTO> findByCriteria(CorrectionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Correction> specification = createSpecification(criteria);
        return correctionRepository.findAll(specification, page)
            .map(correctionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CorrectionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Correction> specification = createSpecification(criteria);
        return correctionRepository.count(specification);
    }

    /**
     * Function to convert {@link CorrectionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Correction> createSpecification(CorrectionCriteria criteria) {
        Specification<Correction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Correction_.id));
            }
            if (criteria.getReporterRemark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReporterRemark(), Correction_.reporterRemark));
            }
            if (criteria.getCorrecterRemark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCorrecterRemark(), Correction_.correcterRemark));
            }
            if (criteria.getResolverRemark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResolverRemark(), Correction_.resolverRemark));
            }
            if (criteria.getTextBeforeCorrection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTextBeforeCorrection(), Correction_.textBeforeCorrection));
            }
            if (criteria.getTextCorrection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTextCorrection(), Correction_.textCorrection));
            }
            if (criteria.getTextAfterCorrection() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTextAfterCorrection(), Correction_.textAfterCorrection));
            }
            if (criteria.getReporterName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReporterName(), Correction_.reporterName));
            }
            if (criteria.getPageURL() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPageURL(), Correction_.pageURL));
            }
            if (criteria.getCorrectionStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrectionStatus(), Correction_.correctionStatus));
            }
            if (criteria.getCommentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommentsId(),
                    root -> root.join(Correction_.comments, JoinType.LEFT).get(Comment_.id)));
            }
            if (criteria.getCorrecterId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrecterId(),
                    root -> root.join(Correction_.correcter, JoinType.LEFT).get(Correcter_.id)));
            }
            if (criteria.getResolverId() != null) {
                specification = specification.and(buildSpecification(criteria.getResolverId(),
                    root -> root.join(Correction_.resolver, JoinType.LEFT).get(Correcter_.id)));
            }
        }
        return specification;
    }
}

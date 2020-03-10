package io.hexlet.hexletcorrection.service;

import io.github.jhipster.service.QueryService;
import io.hexlet.hexletcorrection.domain.Comment_;
import io.hexlet.hexletcorrection.domain.Correction_;
import io.hexlet.hexletcorrection.domain.Preference;
import io.hexlet.hexletcorrection.domain.Preference_;
import io.hexlet.hexletcorrection.domain.User_;
import io.hexlet.hexletcorrection.repository.PreferenceRepository;
import io.hexlet.hexletcorrection.service.dto.PreferenceCriteria;
import io.hexlet.hexletcorrection.service.dto.PreferenceDTO;
import io.hexlet.hexletcorrection.service.mapper.PreferenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link Preference} entities in the database.
 * The main input is a {@link PreferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PreferenceDTO} or a {@link Page} of {@link PreferenceDTO} which fulfills the criteria.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PreferenceQueryService extends QueryService<Preference> {

    private final PreferenceRepository preferenceRepository;

    private final PreferenceMapper preferenceMapper;

    /**
     * Return a {@link List} of {@link PreferenceDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PreferenceDTO> findByCriteria(PreferenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Preference> specification = createSpecification(criteria);
        return preferenceMapper.toDto(preferenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PreferenceDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PreferenceDTO> findByCriteria(PreferenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Preference> specification = createSpecification(criteria);
        return preferenceRepository.findAll(specification, page)
            .map(preferenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PreferenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Preference> specification = createSpecification(criteria);
        return preferenceRepository.count(specification);
    }

    /**
     * Function to convert {@link PreferenceCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Preference> createSpecification(PreferenceCriteria criteria) {
        Specification<Preference> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Preference_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Preference_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCorrectionsInProgressId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrectionsInProgressId(),
                    root -> root.join(Preference_.correctionsInProgresses, JoinType.LEFT).get(Correction_.id)));
            }
            if (criteria.getResolvedCorrectionsId() != null) {
                specification = specification.and(buildSpecification(criteria.getResolvedCorrectionsId(),
                    root -> root.join(Preference_.resolvedCorrections, JoinType.LEFT).get(Correction_.id)));
            }
            if (criteria.getCommentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getCommentsId(),
                    root -> root.join(Preference_.comments, JoinType.LEFT).get(Comment_.id)));
            }
        }
        return specification;
    }
}

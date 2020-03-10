package io.hexlet.hexletcorrection.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Criteria class for the {@link io.hexlet.hexletcorrection.domain.Preference} entity. This class is used
 * in {@link io.hexlet.hexletcorrection.web.rest.PreferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /preferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public class PreferenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private LongFilter correctionsInProgressId;

    private LongFilter resolvedCorrectionsId;

    private LongFilter commentsId;

    public PreferenceCriteria(PreferenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.correctionsInProgressId = other.correctionsInProgressId == null ? null : other.correctionsInProgressId.copy();
        this.resolvedCorrectionsId = other.resolvedCorrectionsId == null ? null : other.resolvedCorrectionsId.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
    }

    @Override
    public PreferenceCriteria copy() {
        return new PreferenceCriteria(this);
    }
}

package io.hexlet.hexletcorrection.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Criteria class for the {@link io.hexlet.hexletcorrection.domain.Comment} entity. This class is used
 * in {@link io.hexlet.hexletcorrection.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private ZonedDateTimeFilter date;

    private LongFilter authorId;

    private LongFilter correctionId;

    public CommentCriteria(CommentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.authorId = other.authorId == null ? null : other.authorId.copy();
        this.correctionId = other.correctionId == null ? null : other.correctionId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
    }
}

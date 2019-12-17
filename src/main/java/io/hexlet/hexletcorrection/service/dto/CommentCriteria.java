package io.hexlet.hexletcorrection.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link io.hexlet.hexletcorrection.domain.Comment} entity. This class is used
 * in {@link io.hexlet.hexletcorrection.web.rest.CommentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /comments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter message;

    private ZonedDateTimeFilter date;

    private LongFilter correctionId;

    private LongFilter correcterId;

    public CommentCriteria(){
    }

    public CommentCriteria(CommentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.correctionId = other.correctionId == null ? null : other.correctionId.copy();
        this.correcterId = other.correcterId == null ? null : other.correcterId.copy();
    }

    @Override
    public CommentCriteria copy() {
        return new CommentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public LongFilter getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(LongFilter correctionId) {
        this.correctionId = correctionId;
    }

    public LongFilter getCorrecterId() {
        return correcterId;
    }

    public void setCorrecterId(LongFilter correcterId) {
        this.correcterId = correcterId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommentCriteria that = (CommentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(message, that.message) &&
            Objects.equals(date, that.date) &&
            Objects.equals(correctionId, that.correctionId) &&
            Objects.equals(correcterId, that.correcterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        message,
        date,
        correctionId,
        correcterId
        );
    }

    @Override
    public String toString() {
        return "CommentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (correctionId != null ? "correctionId=" + correctionId + ", " : "") +
                (correcterId != null ? "correcterId=" + correcterId + ", " : "") +
            "}";
    }

}

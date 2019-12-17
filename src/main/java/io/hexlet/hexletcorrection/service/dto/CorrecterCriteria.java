package io.hexlet.hexletcorrection.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.hexlet.hexletcorrection.domain.enumeration.CorrecterStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.hexlet.hexletcorrection.domain.Correcter} entity. This class is used
 * in {@link io.hexlet.hexletcorrection.web.rest.CorrecterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /correcters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CorrecterCriteria implements Serializable, Criteria {
    /**
     * Class for filtering CorrecterStatus
     */
    public static class CorrecterStatusFilter extends Filter<CorrecterStatus> {

        public CorrecterStatusFilter() {
        }

        public CorrecterStatusFilter(CorrecterStatusFilter filter) {
            super(filter);
        }

        @Override
        public CorrecterStatusFilter copy() {
            return new CorrecterStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private CorrecterStatusFilter status;

    private StringFilter email;

    private StringFilter password;

    private StringFilter phone;

    private LongFilter userId;

    private LongFilter correctionsInProgressId;

    private LongFilter correctionsResolvedId;

    private LongFilter commentsId;

    public CorrecterCriteria(){
    }

    public CorrecterCriteria(CorrecterCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.correctionsInProgressId = other.correctionsInProgressId == null ? null : other.correctionsInProgressId.copy();
        this.correctionsResolvedId = other.correctionsResolvedId == null ? null : other.correctionsResolvedId.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
    }

    @Override
    public CorrecterCriteria copy() {
        return new CorrecterCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public CorrecterStatusFilter getStatus() {
        return status;
    }

    public void setStatus(CorrecterStatusFilter status) {
        this.status = status;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCorrectionsInProgressId() {
        return correctionsInProgressId;
    }

    public void setCorrectionsInProgressId(LongFilter correctionsInProgressId) {
        this.correctionsInProgressId = correctionsInProgressId;
    }

    public LongFilter getCorrectionsResolvedId() {
        return correctionsResolvedId;
    }

    public void setCorrectionsResolvedId(LongFilter correctionsResolvedId) {
        this.correctionsResolvedId = correctionsResolvedId;
    }

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CorrecterCriteria that = (CorrecterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(status, that.status) &&
            Objects.equals(email, that.email) &&
            Objects.equals(password, that.password) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(correctionsInProgressId, that.correctionsInProgressId) &&
            Objects.equals(correctionsResolvedId, that.correctionsResolvedId) &&
            Objects.equals(commentsId, that.commentsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        status,
        email,
        password,
        phone,
        userId,
        correctionsInProgressId,
        correctionsResolvedId,
        commentsId
        );
    }

    @Override
    public String toString() {
        return "CorrecterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (correctionsInProgressId != null ? "correctionsInProgressId=" + correctionsInProgressId + ", " : "") +
                (correctionsResolvedId != null ? "correctionsResolvedId=" + correctionsResolvedId + ", " : "") +
                (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
            "}";
    }

}

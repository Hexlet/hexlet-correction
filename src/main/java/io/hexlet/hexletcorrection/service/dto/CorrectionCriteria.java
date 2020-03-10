package io.hexlet.hexletcorrection.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Criteria class for the {@link io.hexlet.hexletcorrection.domain.Correction} entity. This class is used
 * in {@link io.hexlet.hexletcorrection.web.rest.CorrectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /corrections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@Data
@NoArgsConstructor
public class CorrectionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reporterRemark;

    private StringFilter correcterRemark;

    private StringFilter resolverRemark;

    private StringFilter textBeforeCorrection;

    private StringFilter textCorrection;

    private StringFilter textAfterCorrection;

    private StringFilter reporterName;

    private StringFilter pageURL;

    private CorrectionStatusFilter correctionStatus;

    private LongFilter commentsId;

    private LongFilter correcterId;

    private LongFilter resolverId;

    public CorrectionCriteria(CorrectionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reporterRemark = other.reporterRemark == null ? null : other.reporterRemark.copy();
        this.correcterRemark = other.correcterRemark == null ? null : other.correcterRemark.copy();
        this.resolverRemark = other.resolverRemark == null ? null : other.resolverRemark.copy();
        this.textBeforeCorrection = other.textBeforeCorrection == null ? null : other.textBeforeCorrection.copy();
        this.textCorrection = other.textCorrection == null ? null : other.textCorrection.copy();
        this.textAfterCorrection = other.textAfterCorrection == null ? null : other.textAfterCorrection.copy();
        this.reporterName = other.reporterName == null ? null : other.reporterName.copy();
        this.pageURL = other.pageURL == null ? null : other.pageURL.copy();
        this.correctionStatus = other.correctionStatus == null ? null : other.correctionStatus.copy();
        this.commentsId = other.commentsId == null ? null : other.commentsId.copy();
        this.correcterId = other.correcterId == null ? null : other.correcterId.copy();
        this.resolverId = other.resolverId == null ? null : other.resolverId.copy();
    }

    @Override
    public CorrectionCriteria copy() {
        return new CorrectionCriteria(this);
    }

    /**
     * Class for filtering CorrectionStatus
     */
    @NoArgsConstructor
    public static class CorrectionStatusFilter extends Filter<CorrectionStatus> {

        public CorrectionStatusFilter(CorrectionStatusFilter filter) {
            super(filter);
        }

        @Override
        public CorrectionStatusFilter copy() {
            return new CorrectionStatusFilter(this);
        }
    }
}

package io.hexlet.hexletcorrection.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.hexlet.hexletcorrection.domain.Correction} entity. This class is used
 * in {@link io.hexlet.hexletcorrection.web.rest.CorrectionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /corrections?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CorrectionCriteria implements Serializable, Criteria {
    /**
     * Class for filtering CorrectionStatus
     */
    public static class CorrectionStatusFilter extends Filter<CorrectionStatus> {

        public CorrectionStatusFilter() {
        }

        public CorrectionStatusFilter(CorrectionStatusFilter filter) {
            super(filter);
        }

        @Override
        public CorrectionStatusFilter copy() {
            return new CorrectionStatusFilter(this);
        }

    }

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

    public CorrectionCriteria(){
    }

    public CorrectionCriteria(CorrectionCriteria other){
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

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReporterRemark() {
        return reporterRemark;
    }

    public void setReporterRemark(StringFilter reporterRemark) {
        this.reporterRemark = reporterRemark;
    }

    public StringFilter getCorrecterRemark() {
        return correcterRemark;
    }

    public void setCorrecterRemark(StringFilter correcterRemark) {
        this.correcterRemark = correcterRemark;
    }

    public StringFilter getResolverRemark() {
        return resolverRemark;
    }

    public void setResolverRemark(StringFilter resolverRemark) {
        this.resolverRemark = resolverRemark;
    }

    public StringFilter getTextBeforeCorrection() {
        return textBeforeCorrection;
    }

    public void setTextBeforeCorrection(StringFilter textBeforeCorrection) {
        this.textBeforeCorrection = textBeforeCorrection;
    }

    public StringFilter getTextCorrection() {
        return textCorrection;
    }

    public void setTextCorrection(StringFilter textCorrection) {
        this.textCorrection = textCorrection;
    }

    public StringFilter getTextAfterCorrection() {
        return textAfterCorrection;
    }

    public void setTextAfterCorrection(StringFilter textAfterCorrection) {
        this.textAfterCorrection = textAfterCorrection;
    }

    public StringFilter getReporterName() {
        return reporterName;
    }

    public void setReporterName(StringFilter reporterName) {
        this.reporterName = reporterName;
    }

    public StringFilter getPageURL() {
        return pageURL;
    }

    public void setPageURL(StringFilter pageURL) {
        this.pageURL = pageURL;
    }

    public CorrectionStatusFilter getCorrectionStatus() {
        return correctionStatus;
    }

    public void setCorrectionStatus(CorrectionStatusFilter correctionStatus) {
        this.correctionStatus = correctionStatus;
    }

    public LongFilter getCommentsId() {
        return commentsId;
    }

    public void setCommentsId(LongFilter commentsId) {
        this.commentsId = commentsId;
    }

    public LongFilter getCorrecterId() {
        return correcterId;
    }

    public void setCorrecterId(LongFilter correcterId) {
        this.correcterId = correcterId;
    }

    public LongFilter getResolverId() {
        return resolverId;
    }

    public void setResolverId(LongFilter resolverId) {
        this.resolverId = resolverId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CorrectionCriteria that = (CorrectionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reporterRemark, that.reporterRemark) &&
            Objects.equals(correcterRemark, that.correcterRemark) &&
            Objects.equals(resolverRemark, that.resolverRemark) &&
            Objects.equals(textBeforeCorrection, that.textBeforeCorrection) &&
            Objects.equals(textCorrection, that.textCorrection) &&
            Objects.equals(textAfterCorrection, that.textAfterCorrection) &&
            Objects.equals(reporterName, that.reporterName) &&
            Objects.equals(pageURL, that.pageURL) &&
            Objects.equals(correctionStatus, that.correctionStatus) &&
            Objects.equals(commentsId, that.commentsId) &&
            Objects.equals(correcterId, that.correcterId) &&
            Objects.equals(resolverId, that.resolverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reporterRemark,
        correcterRemark,
        resolverRemark,
        textBeforeCorrection,
        textCorrection,
        textAfterCorrection,
        reporterName,
        pageURL,
        correctionStatus,
        commentsId,
        correcterId,
        resolverId
        );
    }

    @Override
    public String toString() {
        return "CorrectionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reporterRemark != null ? "reporterRemark=" + reporterRemark + ", " : "") +
                (correcterRemark != null ? "correcterRemark=" + correcterRemark + ", " : "") +
                (resolverRemark != null ? "resolverRemark=" + resolverRemark + ", " : "") +
                (textBeforeCorrection != null ? "textBeforeCorrection=" + textBeforeCorrection + ", " : "") +
                (textCorrection != null ? "textCorrection=" + textCorrection + ", " : "") +
                (textAfterCorrection != null ? "textAfterCorrection=" + textAfterCorrection + ", " : "") +
                (reporterName != null ? "reporterName=" + reporterName + ", " : "") +
                (pageURL != null ? "pageURL=" + pageURL + ", " : "") +
                (correctionStatus != null ? "correctionStatus=" + correctionStatus + ", " : "") +
                (commentsId != null ? "commentsId=" + commentsId + ", " : "") +
                (correcterId != null ? "correcterId=" + correcterId + ", " : "") +
                (resolverId != null ? "resolverId=" + resolverId + ", " : "") +
            "}";
    }

}

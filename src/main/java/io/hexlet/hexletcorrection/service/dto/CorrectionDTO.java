package io.hexlet.hexletcorrection.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;

/**
 * A DTO for the {@link io.hexlet.hexletcorrection.domain.Correction} entity.
 */
public class CorrectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 200)
    private String reporterRemark;

    @Size(max = 200)
    private String correcterRemark;

    @Size(max = 200)
    private String resolverRemark;

    @Size(max = 1000)
    private String textBeforeCorrection;

    @NotNull
    @Size(min = 1, max = 1000)
    private String textCorrection;

    @Size(max = 1000)
    private String textAfterCorrection;

    @NotNull
    @Size(min = 1, max = 50)
    private String reporterName;

    @NotNull
    @Size(max = 50)
    private String pageURL;

    @NotNull
    private CorrectionStatus correctionStatus;


    private Long correcterId;

    private Long resolverId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReporterRemark() {
        return reporterRemark;
    }

    public void setReporterRemark(String reporterRemark) {
        this.reporterRemark = reporterRemark;
    }

    public String getCorrecterRemark() {
        return correcterRemark;
    }

    public void setCorrecterRemark(String correcterRemark) {
        this.correcterRemark = correcterRemark;
    }

    public String getResolverRemark() {
        return resolverRemark;
    }

    public void setResolverRemark(String resolverRemark) {
        this.resolverRemark = resolverRemark;
    }

    public String getTextBeforeCorrection() {
        return textBeforeCorrection;
    }

    public void setTextBeforeCorrection(String textBeforeCorrection) {
        this.textBeforeCorrection = textBeforeCorrection;
    }

    public String getTextCorrection() {
        return textCorrection;
    }

    public void setTextCorrection(String textCorrection) {
        this.textCorrection = textCorrection;
    }

    public String getTextAfterCorrection() {
        return textAfterCorrection;
    }

    public void setTextAfterCorrection(String textAfterCorrection) {
        this.textAfterCorrection = textAfterCorrection;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public CorrectionStatus getCorrectionStatus() {
        return correctionStatus;
    }

    public void setCorrectionStatus(CorrectionStatus correctionStatus) {
        this.correctionStatus = correctionStatus;
    }

    public Long getCorrecterId() {
        return correcterId;
    }

    public void setCorrecterId(Long correcterId) {
        this.correcterId = correcterId;
    }

    public Long getResolverId() {
        return resolverId;
    }

    public void setResolverId(Long correcterId) {
        this.resolverId = correcterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CorrectionDTO correctionDTO = (CorrectionDTO) o;
        if (correctionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), correctionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorrectionDTO{" +
            "id=" + getId() +
            ", reporterRemark='" + getReporterRemark() + "'" +
            ", correcterRemark='" + getCorrecterRemark() + "'" +
            ", resolverRemark='" + getResolverRemark() + "'" +
            ", textBeforeCorrection='" + getTextBeforeCorrection() + "'" +
            ", textCorrection='" + getTextCorrection() + "'" +
            ", textAfterCorrection='" + getTextAfterCorrection() + "'" +
            ", reporterName='" + getReporterName() + "'" +
            ", pageURL='" + getPageURL() + "'" +
            ", correctionStatus='" + getCorrectionStatus() + "'" +
            ", correcter=" + getCorrecterId() +
            ", resolver=" + getResolverId() +
            "}";
    }
}

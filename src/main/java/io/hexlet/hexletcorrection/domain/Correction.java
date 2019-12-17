package io.hexlet.hexletcorrection.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;

/**
 * A Correction.
 */
@Entity
@Table(name = "correction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Correction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 200)
    @Column(name = "reporter_remark", length = 200)
    private String reporterRemark;

    @Size(max = 200)
    @Column(name = "correcter_remark", length = 200)
    private String correcterRemark;

    @Size(max = 200)
    @Column(name = "resolver_remark", length = 200)
    private String resolverRemark;

    @Size(max = 1000)
    @Column(name = "text_before_correction", length = 1000)
    private String textBeforeCorrection;

    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "text_correction", length = 1000, nullable = false)
    private String textCorrection;

    @Size(max = 1000)
    @Column(name = "text_after_correction", length = 1000)
    private String textAfterCorrection;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "reporter_name", length = 50, nullable = false)
    private String reporterName;

    @NotNull
    @Size(max = 50)
    @Column(name = "page_url", length = 50, nullable = false)
    private String pageURL;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "correction_status", nullable = false)
    private CorrectionStatus correctionStatus;

    @OneToMany(mappedBy = "correction")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("correctionsInProgresses")
    private Correcter correcter;

    @ManyToOne
    @JsonIgnoreProperties("correctionsResolveds")
    private Correcter resolver;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReporterRemark() {
        return reporterRemark;
    }

    public Correction reporterRemark(String reporterRemark) {
        this.reporterRemark = reporterRemark;
        return this;
    }

    public void setReporterRemark(String reporterRemark) {
        this.reporterRemark = reporterRemark;
    }

    public String getCorrecterRemark() {
        return correcterRemark;
    }

    public Correction correcterRemark(String correcterRemark) {
        this.correcterRemark = correcterRemark;
        return this;
    }

    public void setCorrecterRemark(String correcterRemark) {
        this.correcterRemark = correcterRemark;
    }

    public String getResolverRemark() {
        return resolverRemark;
    }

    public Correction resolverRemark(String resolverRemark) {
        this.resolverRemark = resolverRemark;
        return this;
    }

    public void setResolverRemark(String resolverRemark) {
        this.resolverRemark = resolverRemark;
    }

    public String getTextBeforeCorrection() {
        return textBeforeCorrection;
    }

    public Correction textBeforeCorrection(String textBeforeCorrection) {
        this.textBeforeCorrection = textBeforeCorrection;
        return this;
    }

    public void setTextBeforeCorrection(String textBeforeCorrection) {
        this.textBeforeCorrection = textBeforeCorrection;
    }

    public String getTextCorrection() {
        return textCorrection;
    }

    public Correction textCorrection(String textCorrection) {
        this.textCorrection = textCorrection;
        return this;
    }

    public void setTextCorrection(String textCorrection) {
        this.textCorrection = textCorrection;
    }

    public String getTextAfterCorrection() {
        return textAfterCorrection;
    }

    public Correction textAfterCorrection(String textAfterCorrection) {
        this.textAfterCorrection = textAfterCorrection;
        return this;
    }

    public void setTextAfterCorrection(String textAfterCorrection) {
        this.textAfterCorrection = textAfterCorrection;
    }

    public String getReporterName() {
        return reporterName;
    }

    public Correction reporterName(String reporterName) {
        this.reporterName = reporterName;
        return this;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getPageURL() {
        return pageURL;
    }

    public Correction pageURL(String pageURL) {
        this.pageURL = pageURL;
        return this;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public CorrectionStatus getCorrectionStatus() {
        return correctionStatus;
    }

    public Correction correctionStatus(CorrectionStatus correctionStatus) {
        this.correctionStatus = correctionStatus;
        return this;
    }

    public void setCorrectionStatus(CorrectionStatus correctionStatus) {
        this.correctionStatus = correctionStatus;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Correction comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Correction addComments(Comment comment) {
        this.comments.add(comment);
        comment.setCorrection(this);
        return this;
    }

    public Correction removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setCorrection(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Correcter getCorrecter() {
        return correcter;
    }

    public Correction correcter(Correcter correcter) {
        this.correcter = correcter;
        return this;
    }

    public void setCorrecter(Correcter correcter) {
        this.correcter = correcter;
    }

    public Correcter getResolver() {
        return resolver;
    }

    public Correction resolver(Correcter correcter) {
        this.resolver = correcter;
        return this;
    }

    public void setResolver(Correcter correcter) {
        this.resolver = correcter;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Correction)) {
            return false;
        }
        return id != null && id.equals(((Correction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Correction{" +
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
            "}";
    }
}

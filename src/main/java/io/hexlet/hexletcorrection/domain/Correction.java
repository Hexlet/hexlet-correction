package io.hexlet.hexletcorrection.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

/**
 * A Correction.
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "correction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Correction extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 1000)
    @Column(name = "reporter_remark", length = 1000)
    private String reporterRemark;

    @Size(max = 1000)
    @Column(name = "correcter_remark", length = 1000)
    private String correcterRemark;

    @Size(max = 1000)
    @Column(name = "resolver_remark", length = 1000)
    private String resolverRemark;

    @Size(max = 100)
    @Column(name = "text_before_correction", length = 100)
    private String textBeforeCorrection;

    @NotNull
    @Size(max = 100)
    @Column(name = "text_correction", length = 100, nullable = false)
    private String textCorrection;

    @Size(max = 100)
    @Column(name = "text_after_correction", length = 100)
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

    @OneToMany(mappedBy = "correction", cascade = ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne(fetch = LAZY)
    @JsonIgnoreProperties({"resolvedCorrections", "correctionsInProgresses"})
    private Preference correcter;

    @ManyToOne(fetch = LAZY)
    @JsonIgnoreProperties({"resolvedCorrections", "correctionsInProgresses"})
    private Preference resolver;

    public void addComment(final Comment comment) {
        comments.add(comment);
        comment.setCorrection(this);
    }

    public void removeComment(final Comment comment) {
        comments.remove(comment);
        comment.setCorrection(null);
    }

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
}

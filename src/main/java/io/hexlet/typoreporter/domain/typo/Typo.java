package io.hexlet.typoreporter.domain.typo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.typo.constraint.ReporterComment;
import io.hexlet.typoreporter.domain.typo.constraint.ReporterName;
import io.hexlet.typoreporter.domain.typo.constraint.TextAfterTypo;
import io.hexlet.typoreporter.domain.typo.constraint.TextBeforeTypo;
import io.hexlet.typoreporter.domain.typo.constraint.TextTypo;
import io.hexlet.typoreporter.domain.typo.constraint.TypoPageUrl;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Typo extends AbstractAuditingEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typo_id_seq")
    @SequenceGenerator(name = "typo_id_seq", sequenceName = "common_seq_id")
    private Long id;

    @TypoPageUrl
    private String pageUrl;

    @ReporterName
    private String reporterName;

    @ReporterComment
    private String reporterComment;

    @TextBeforeTypo
    private String textBeforeTypo;

    @TextTypo
    private String textTypo;

    @TextAfterTypo
    private String textAfterTypo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private TypoStatus typoStatus = TypoStatus.REPORTED;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Account account;

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || id != null && obj instanceof Typo other && id.equals(other.id);
    }
}

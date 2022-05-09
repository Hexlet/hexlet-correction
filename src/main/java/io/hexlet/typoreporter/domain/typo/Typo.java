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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@TypeDef(name = "pgsql_typo_status_enum", typeClass = TypoStatusPgEnum.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Typo extends AbstractAuditingEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "typo_id_seq")
    @SequenceGenerator(name = "typo_id_seq", allocationSize = 15)
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
    @Column(columnDefinition = "TYPO_STATUS")
    @Type(type = "pgsql_typo_status_enum")
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

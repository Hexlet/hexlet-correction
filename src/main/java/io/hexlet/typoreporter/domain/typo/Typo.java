package io.hexlet.typoreporter.domain.typo;

import io.hexlet.typoreporter.domain.*;
import io.hexlet.typoreporter.domain.typo.constraint.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@TypeDef(name = "pgsql_enum", typeClass = TypoStatusPgEnum.class)
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
    @Type(type = "pgsql_enum")
    private TypoStatus typoStatus = TypoStatus.REPORTED;

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || id != null && obj instanceof Typo other && id.equals(other.id);
    }
}

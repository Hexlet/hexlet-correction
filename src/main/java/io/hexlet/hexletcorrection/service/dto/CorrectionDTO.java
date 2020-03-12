package io.hexlet.hexletcorrection.service.dto;

import io.hexlet.hexletcorrection.domain.enumeration.CorrectionStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.hexlet.hexletcorrection.domain.Correction} entity.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CorrectionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Size(max = 1000)
    private String reporterRemark;

    @Size(max = 1000)
    private String correcterRemark;

    @Size(max = 1000)
    private String resolverRemark;

    @Size(max = 100)
    private String textBeforeCorrection;

    @NotNull
    @Size(max = 100)
    private String textCorrection;

    @Size(max = 100)
    private String textAfterCorrection;

    @NotNull
    @Size(min = 1, max = 50)
    private String reporterName;

    @NotNull
    @Size(max = 50)
    private String pageURL;

    private CorrectionStatus correctionStatus;

    private Long correcterId;

    private Long resolverId;

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
}

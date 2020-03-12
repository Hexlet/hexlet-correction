package io.hexlet.hexletcorrection.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.hexlet.hexletcorrection.domain.Preference} entity.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PreferenceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @Lob
    @ToString.Exclude
    private byte[] avatar;

    private String avatarContentType;

    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PreferenceDTO preferenceDTO = (PreferenceDTO) o;
        if (preferenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), preferenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}

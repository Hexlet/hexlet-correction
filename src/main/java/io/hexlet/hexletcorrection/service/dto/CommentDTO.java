package io.hexlet.hexletcorrection.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link io.hexlet.hexletcorrection.domain.Comment} entity.
 */
@Getter
@Setter
@ToString
public class CommentDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String message;

    @NotNull
    private ZonedDateTime date;

    private Long authorId;

    private Long correctionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (commentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}

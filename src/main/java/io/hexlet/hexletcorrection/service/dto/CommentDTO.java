package io.hexlet.hexletcorrection.service.dto;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.hexlet.hexletcorrection.domain.Comment} entity.
 */
public class CommentDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String message;

    @NotNull
    private ZonedDateTime date;


    private Long correctionId;

    private Long correcterId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Long getCorrectionId() {
        return correctionId;
    }

    public void setCorrectionId(Long correctionId) {
        this.correctionId = correctionId;
    }

    public Long getCorrecterId() {
        return correcterId;
    }

    public void setCorrecterId(Long correcterId) {
        this.correcterId = correcterId;
    }

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

    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", message='" + getMessage() + "'" +
            ", date='" + getDate() + "'" +
            ", correction=" + getCorrectionId() +
            ", correcter=" + getCorrecterId() +
            "}";
    }
}

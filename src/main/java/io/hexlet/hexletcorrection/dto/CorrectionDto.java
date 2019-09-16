package io.hexlet.hexletcorrection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_COMMENT_LENGTH;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_NULL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CorrectionDto {

    private Long id;

    @NotEmpty(message = "Comment " + NOT_EMPTY)
    @Size(message = "Comment not be more than " + MAX_COMMENT_LENGTH + " characters", max = MAX_COMMENT_LENGTH)
    private String comment;

    @Column(name = "highlight_text", nullable = false)
    @NotEmpty(message = "Highlight text " + NOT_EMPTY)
    private String highlightText;

    @NotNull(message = "Account " + NOT_NULL)
    @JsonIgnoreProperties("corrections")
    private AccountDto account;

    @NotBlank(message = "URL " + NOT_EMPTY)
    private String pageURL;
}

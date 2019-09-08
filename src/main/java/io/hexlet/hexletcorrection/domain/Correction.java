package io.hexlet.hexletcorrection.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_COMMENT_LENGTH;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_NULL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Correction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Comment " + NOT_EMPTY)
    @Size(message = "Comment not be more than " + MAX_COMMENT_LENGTH + " characters", max = MAX_COMMENT_LENGTH)
    private String comment;

    @NotEmpty(message = "Highlight text " + NOT_EMPTY)
    private String highlightText;

    @NotNull(message = "User " + NOT_NULL)
    @ManyToOne
    private User user;

    @NotBlank(message = "URL " + NOT_EMPTY)
    private String pageURL;
}

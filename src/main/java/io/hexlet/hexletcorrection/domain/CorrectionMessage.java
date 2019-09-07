package io.hexlet.hexletcorrection.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CorrectionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(message = "comment not be more than 200 characters", max = 200)
    private String comment;

    @NotNull(message = "highlight text not be null")
    private String highlightText;

    @NotNull(message = "User not be null")
    @ManyToOne
    private User user;

    @NotNull(message = "URL not be null")
    @NotBlank(message = "URL not be empty")
    @URL(message = "Invalid URL provided")
    private String pageURL;
}

package io.hexlet.hexletcorrection.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.INVALID_EMAIL;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_ACCOUNT_NAME;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDto {

    private Long id;

    @NotBlank(message = "Name " + NOT_EMPTY)
    @Size(message = "Name not be more than " + MAX_ACCOUNT_NAME + " characters", max = MAX_ACCOUNT_NAME)
    private String name;

    @NotBlank(message = "Email " + NOT_EMPTY)
    @Email(message = INVALID_EMAIL)
    private String email;

    @JsonIgnoreProperties("account")
    private Set<CorrectionDto> corrections;
}

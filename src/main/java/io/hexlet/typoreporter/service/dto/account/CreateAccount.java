package io.hexlet.typoreporter.service.dto.account;

import javax.validation.constraints.*;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccount {

    @AccountUsername
    private String username;

    @Email
    private String email;

    @AccountPassword
    private String password;

    private String confirmPassword;

    @NotBlank @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank @Size(min = 1, max = 50)
    private String lastName;
}

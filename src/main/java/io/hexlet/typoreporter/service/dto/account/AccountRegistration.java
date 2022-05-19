package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegistration {

    private static final int MAX_FIRST_LAST_NAME = 50;

    @NotBlank
    private String externalOpenId;

    @NotNull
    private AuthProvider authProvider;

    @Email(message = "Email should be valid")
    private String email;

    @AccountUsername
    private String username;

    private String password;

    @NotBlank
    @Size(min = 1, max = MAX_FIRST_LAST_NAME, message = "firstName longer than 1 character")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = MAX_FIRST_LAST_NAME, message = "lastName longer than 1 character")
    private String lastName;

}

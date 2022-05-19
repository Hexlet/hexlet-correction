package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegistration {

    private static final int MAX_FIRST_LAST_NAME = 50;
    private static final int MAX_PASSWORD = 30;

    //    @NotBlank
    private String externalOpenId;

    //    @NotNull
    private AuthProvider authProvider;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @AccountUsername
    private String username;

    @Size(min = 8, max = MAX_PASSWORD, message = "Password size must be between 8 and 30 characters")
    private String password;

    //    @NotBlank(message = "First name must not be null")
    @NotBlank
    @Size(min = 1, max = MAX_FIRST_LAST_NAME, message = "firstName longer than 1 character")
    private String firstName;

    //    @NotBlank(message = "Last name must not be null")
    @NotBlank
    @Size(min = 1, max = MAX_FIRST_LAST_NAME, message = "lastName longer than 1 character")
    private String lastName;

}

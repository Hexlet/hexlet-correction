package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import io.hexlet.typoreporter.service.dto.FieldMatch;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password and it confirmation must match"),
    @FieldMatch(first = "email", second = "confirmEmail", message = "The email '{first}' and it confirmation '{second}' must match")
})
public class SignupAccount {

    @AccountUsername
    private String username;

    @Email
    private String email;

    @Email
    private String confirmEmail;

    @AccountPassword
    private String password;

    @AccountPassword
    private String confirmPassword;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;
}

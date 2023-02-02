package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import io.hexlet.typoreporter.service.dto.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@FieldMatch.List({
    @FieldMatch(first = "password", second = "confirmPassword", message = "The password and it confirmation must match"),
    @FieldMatch(first = "email", second = "confirmEmail", message = "The email \"{0}\" and it confirmation \"{1}\" must match")
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

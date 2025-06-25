package io.hexlet.typoreporter.web.model;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import io.hexlet.typoreporter.service.dto.FieldMatchConsiderCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldMatchConsiderCase(
    first = "password",
    second = "confirmPassword",
    message = "{alert.passwords-dont-match}")
@ToString
public class SignupAccountModel {

    @AccountUsername
    private String username;

    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
        message = "The email \"${validatedValue}\" is not valid")
    private String email;

    @AccountPassword
    @ToString.Exclude
    private String password;

    @AccountPassword
    @ToString.Exclude
    private String confirmPassword;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;


    private String authProvider;
}

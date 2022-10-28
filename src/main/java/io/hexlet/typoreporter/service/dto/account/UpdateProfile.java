package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateProfile {

    @AccountUsername
    private String username;

    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;
}

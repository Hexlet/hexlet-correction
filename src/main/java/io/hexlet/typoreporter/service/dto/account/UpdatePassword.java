package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.PasswordsMustMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@PasswordsMustMatch(message = "The password fields must match")
public class UpdatePassword {

    private String oldPassword;

    @AccountPassword
    private String newPassword;

    private String confirmPassword;
}

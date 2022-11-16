package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.service.dto.FieldMatch;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "The password and it confirmation must match")
public class UpdatePassword {

    private String oldPassword;

    @AccountPassword
    private String newPassword;

    @AccountPassword
    private String confirmNewPassword;
}

package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UpdatePassword {

    private String oldPassword;

    @AccountPassword
    private String newPassword;

    private String confirmPassword;
}

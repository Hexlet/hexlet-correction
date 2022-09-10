package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAccount {

    @AccountUsername
    private String username;

    @AccountPassword
    private String password;

}

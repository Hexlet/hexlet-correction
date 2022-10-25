package io.hexlet.typoreporter.service.dto.account;

import io.hexlet.typoreporter.domain.account.constraint.AccountPassword;
import io.hexlet.typoreporter.domain.account.constraint.AccountUsername;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginAccount {

    @AccountUsername
    private String username;

    @AccountPassword
    private String password;
}

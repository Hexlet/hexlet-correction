package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;

public interface SignUpAccount {

    Account signup(SignupAccount signupAccount);
}

package io.hexlet.typoreporter.service.account.signup;

import io.hexlet.typoreporter.service.account.EmailAlreadyExistException;
import io.hexlet.typoreporter.service.account.UsernameAlreadyExistException;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;

public interface SignupAccountUseCase {

    InfoAccount signup(SignupAccount signupAccount) throws UsernameAlreadyExistException, EmailAlreadyExistException;
}

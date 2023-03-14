package io.hexlet.typoreporter.web.model;

import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import org.mapstruct.Mapper;

@Mapper
public interface AccountModelMapper {

    SignupAccount toSignupAccount(SignupAccountModel signupAccountModel);
}

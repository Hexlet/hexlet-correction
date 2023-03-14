package io.hexlet.typoreporter.service.account.signup;

import io.hexlet.typoreporter.web.model.SignupAccountModel;
import org.mapstruct.Mapper;

@Mapper
public interface SignupAccountMapper {

    SignupAccount toSignupAccount(SignupAccountModel signupAccountModel);
}

package io.hexlet.typoreporter.web.model;

import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T23:33:48+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class AccountModelMapperImpl implements AccountModelMapper {

    @Override
    public SignupAccount toSignupAccount(SignupAccountModel signupAccountModel) {
        if ( signupAccountModel == null ) {
            return null;
        }

        String username = null;
        String email = null;
        String password = null;
        String firstName = null;
        String lastName = null;

        username = signupAccountModel.getUsername();
        email = signupAccountModel.getEmail();
        password = signupAccountModel.getPassword();
        firstName = signupAccountModel.getFirstName();
        lastName = signupAccountModel.getLastName();

        SignupAccount signupAccount = new SignupAccount( username, email, password, firstName, lastName );

        return signupAccount;
    }
}

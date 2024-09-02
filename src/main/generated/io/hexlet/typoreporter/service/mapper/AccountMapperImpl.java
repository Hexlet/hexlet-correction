package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T23:33:48+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public InfoAccount toInfoAccount(Account source) {
        if ( source == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String username = null;
        String firstName = null;
        String lastName = null;

        id = source.getId();
        email = source.getEmail();
        username = source.getUsername();
        firstName = source.getFirstName();
        lastName = source.getLastName();

        InfoAccount infoAccount = new InfoAccount( id, email, username, firstName, lastName );

        return infoAccount;
    }

    @Override
    public UpdateProfile toUpdateProfile(Account source) {
        if ( source == null ) {
            return null;
        }

        UpdateProfile updateProfile = new UpdateProfile();

        updateProfile.setUsername( source.getUsername() );
        updateProfile.setEmail( source.getEmail() );
        updateProfile.setFirstName( source.getFirstName() );
        updateProfile.setLastName( source.getLastName() );

        return updateProfile;
    }

    @Override
    public Account toAccount(UpdateProfile source, Account account) {
        if ( source == null ) {
            return account;
        }

        account.setEmail( source.getEmail() );
        account.setUsername( source.getUsername() );
        account.setFirstName( source.getFirstName() );
        account.setLastName( source.getLastName() );

        return account;
    }

    @Override
    public Account toAccount(SignupAccount source) {
        if ( source == null ) {
            return null;
        }

        Account account = new Account();

        account.setEmail( source.email() );
        account.setUsername( source.username() );
        account.setPassword( source.password() );
        account.setFirstName( source.firstName() );
        account.setLastName( source.lastName() );

        return account;
    }
}

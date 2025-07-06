package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface AccountMapper {

    InfoAccount toInfoAccount(Account source);

    UpdateProfile toUpdateProfile(Account source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalOpenId", ignore = true)
    @Mapping(target = "workspaceRoles", ignore = true)
    @Mapping(target = "typos", ignore = true)
    @Mapping(target = "removeTypo", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authProvider", ignore = true)
    Account toAccount(UpdateProfile source, @MappingTarget Account account);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalOpenId", ignore = true)
    @Mapping(target = "workspaceRoles", ignore = true)
    @Mapping(target = "typos", ignore = true)
    @Mapping(target = "removeTypo", ignore = true) // Игнорирование метода
    @Mapping(target = "authProvider", expression = "java(mapAuthProvider(source.authProvider()))")
    Account toAccount(SignupAccount source);

    default AuthProvider mapAuthProvider(String authProvider) {
        if (authProvider == null) {
            return null;
        }
        return AuthProvider.valueOf(authProvider.toUpperCase());
    }
}

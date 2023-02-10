package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {WorkspaceMapper.class, TypoMapper.class})
public interface AccountMapper {
    InfoAccount toInfoAccount(Account source);
    Account toAccount(InfoAccount source);
    UpdateProfile toUpdateProfile(Account source);
    Account toAccount(UpdateProfile source);
    Account toAccount(SignupAccount source);
    SignupAccount toSignupAccount(Account source);
}

package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface AccountMapper {

    InfoAccount toInfoAccount(Account source);
    UpdateProfile toUpdateProfile(Account source);
    Account toAccount(UpdateProfile source, @MappingTarget Account account);
    Account toAccount(SignupAccount source);
}

package io.hexlet.typoreporter.service.account;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.constraint.EncodedMapping;
import io.hexlet.typoreporter.service.PasswordEncoderMapper;
import io.hexlet.typoreporter.service.dto.account.AccountRegistration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface AccountMapper {
    @Mapping(target = "authProvider", defaultValue = "EMAIL")
    @Mapping(target = "externalOpenId", defaultValue = "11_11_11")
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    Account toAccountRegistration(AccountRegistration accountRegistration);

}

package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CorrectionMapper {

    @Mapping(target = "account", qualifiedByName = "accountToAccountDto")
    CorrectionDto toCorrectionDto(Correction correction);

    @Named("accountToAccountDto")
    @Mapping(target = "corrections", expression = "java(null)")
    AccountDto toAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    Correction toCorrection(CorrectionDto correctionDto);

    @Mapping(target = "corrections", ignore = true)
    Account toAccount(AccountDto accountDto);
}

package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import io.hexlet.hexletcorrection.dto.CorrectionPostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "account.name", ignore = true),
            @Mapping(target = "account.email", ignore = true),
            @Mapping(target = "account.corrections", ignore = true),
    })
    Correction postDtoToCorrection(CorrectionPostDto correctionPostDto);
}

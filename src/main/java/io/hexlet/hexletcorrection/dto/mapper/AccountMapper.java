package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "corrections", qualifiedByName = "correctionsToCorrectionsDto")
    AccountDto accountToAccountDto(Account account);

    @Named("correctionsToCorrectionsDto")
    @Mapping(target = "account", expression = "java(null)")
    CorrectionDto correctionsToCorrectionsDto(Correction correction);

    @Mappings({
            @Mapping(target = "corrections", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    Account accountDtoToAccount(AccountDto accountDto);
}

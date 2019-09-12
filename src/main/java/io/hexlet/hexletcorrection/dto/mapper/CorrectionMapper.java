package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CorrectionMapper {

    CorrectionMapper INSTANCE = Mappers.getMapper(CorrectionMapper.class);

    @Mapping(target = "account", qualifiedByName = "accountToAccountDto")
    CorrectionDto correctionToCorrectionDto(Correction correction);

    @Named("accountToAccountDto")
    @Mapping(target = "corrections", expression = "java(null)")
    AccountDto accountToAccountDto(Account account);

    @Mapping(target = "id", ignore = true)
    Correction correctionDtoToCorrection(CorrectionDto correctionDto);

    @Mapping(target = "corrections", ignore = true)
    Account accountDtoToAccount(AccountDto accountDto);
}

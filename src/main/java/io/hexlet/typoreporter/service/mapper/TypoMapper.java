package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, TypoMapper.class})
public interface TypoMapper {
    TypoReport toTypoReport(Typo source);
    Typo toTypo(TypoReport source);
    TypoInfo toTypoInfo(Typo source);
    Typo toTypo(TypoInfo source);
    ReportedTypo toReportedTypo(Typo source);
    Typo toTypo(ReportedTypo source);
}

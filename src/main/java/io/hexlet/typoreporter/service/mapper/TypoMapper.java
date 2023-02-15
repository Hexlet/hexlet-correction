package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;

@Mapper
public interface TypoMapper {

    PrettyTime prettyTime = new PrettyTime();

    Typo toTypo(TypoReport source);

    @Mapping(target = "modifiedDateAgo", source = "modifiedDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "createdDateAgo", source = "createdDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "typoStatusStr", source = "typoStatus")
    TypoInfo toTypoInfo(Typo source);

    ReportedTypo toReportedTypo(Typo source);

    @Named(value = "mapToPrettyDateAgo")
    default String getDateAgoAsString(LocalDateTime date) {
        return prettyTime.format(date);
    }
}

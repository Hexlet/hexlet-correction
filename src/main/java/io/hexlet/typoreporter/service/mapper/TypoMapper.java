package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Instant;

@Mapper
public interface TypoMapper {

    PrettyTime PRETTY_TIME = new PrettyTime();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "typoStatus", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    Typo toTypo(TypoReport source);

    @Mapping(target = "modifiedDateAgo", source = "modifiedDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "createdDateAgo", source = "createdDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "typoStatusStr", source = "typoStatus")
    TypoInfo toTypoInfo(Typo source);

    ReportedTypo toReportedTypo(Typo source);

    @Named(value = "mapToPrettyDateAgo")
    default String getDateAgoAsString(Instant date) {
        PRETTY_TIME.setLocale(LocaleContextHolder.getLocale());
        return PRETTY_TIME.format(date);
    }
}

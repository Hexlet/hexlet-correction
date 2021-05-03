package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TypoReportToTypoConverter implements Converter<TypoReport, Typo> {

    /**
     * Convert the source object of type {@code TypoReport} to target type {@code Typo}.
     *
     * @param source the source object to convert, which must be an instance of {@code TypoReport} (never {@code null})
     * @return the converted object, which must be an instance of {@code Typo} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public Typo convert(TypoReport source) {
        return new Typo()
            .setPageUrl(source.pageUrl())
            .setReporterName(source.reporterName())
            .setReporterComment(source.reporterComment())
            .setTextBeforeTypo(source.textBeforeTypo())
            .setTextTypo(source.textTypo())
            .setTextAfterTypo(source.textAfterTypo());
    }
}

package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TypoToReportedTypoConverter implements Converter<Typo, ReportedTypo> {

    /**
     * Convert the source object of type {@code Typo} to target type {@code ReportedTypo}.
     *
     * @param typo the source object to convert, which must be an instance of {@code Typo} (never {@code null})
     * @return the converted object, which must be an instance of {@code ReportedTypo} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ReportedTypo convert(Typo typo) {
        return new ReportedTypo(
                typo.getId(),
                typo.getPageUrl(),
                typo.getReporterName(),
                typo.getReporterComment(),
                typo.getTextBeforeTypo(),
                typo.getTextTypo(),
                typo.getTextAfterTypo(),
                typo.getCreatedBy(),
                typo.getCreatedDate()
        );
    }
}

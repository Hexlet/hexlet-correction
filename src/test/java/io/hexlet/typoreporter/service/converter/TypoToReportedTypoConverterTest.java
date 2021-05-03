package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.converter.Converter;

import static io.hexlet.typoreporter.test.asserts.ReportedTypoAssert.assertThat;

class TypoToReportedTypoConverterTest {

    private final Converter<Typo, ReportedTypo> converter = new TypoToReportedTypoConverter();

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void typoToResponseReport(final Typo typo) {
        final var reportedTypo = converter.convert(typo);
        assertThat(reportedTypo).isEqualsToTypo(typo);
    }
}

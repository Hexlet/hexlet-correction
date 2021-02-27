package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.converter.Converter;

import static io.hexlet.typoreporter.test.asserts.TypoAssert.assertThat;

class TypoReportToTypoConverterTest {

    private final Converter<TypoReport, Typo> converter = new TypoReportToTypoConverter();

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoReport")
    void requestReportToTypo(final TypoReport report) {
        final var typo = converter.convert(report);
        assertThat(typo).isEqualsToTypoReport(report);
    }
}

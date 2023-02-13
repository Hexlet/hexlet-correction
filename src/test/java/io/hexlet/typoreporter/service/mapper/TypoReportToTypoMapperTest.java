package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import static io.hexlet.typoreporter.test.asserts.TypoAssert.assertThat;

class TypoReportToTypoMapperTest {
    private final TypoMapper typoMapper = Mappers.getMapper(TypoMapper.class);

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoReport")
    void requestReportToTypo(final TypoReport report) {
        final var typo = typoMapper.toTypo(report);
        assertThat(typo).isEqualsToTypoReport(report);
    }
}

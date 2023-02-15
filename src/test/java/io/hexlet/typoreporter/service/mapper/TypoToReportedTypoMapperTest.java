package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import static io.hexlet.typoreporter.test.asserts.ReportedTypoAssert.assertThat;

class TypoToReportedTypoMapperTest {
    private final TypoMapper typoMapper = Mappers.getMapper(TypoMapper.class);

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void typoToResponseReport(final Typo typo) {
        final var reportedTypo = typoMapper.toReportedTypo(typo);
        assertThat(reportedTypo).isEqualsToTypo(typo);
    }
}

package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.typo.Typo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.hexlet.typoreporter.test.asserts.ReportedTypoAssert.assertThat;

@SpringBootTest(classes = {TypoMapperImpl.class})
class TypoToReportedTypoMapperTest {
    @Autowired
    private TypoMapper typoMapper;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void typoToResponseReport(final Typo typo) {
        final var reportedTypo = typoMapper.toReportedTypo(typo);
        assertThat(reportedTypo).isEqualsToTypo(typo);
    }
}

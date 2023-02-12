package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.hexlet.typoreporter.test.asserts.TypoAssert.assertThat;

@SpringBootTest(classes = {TypoMapperImpl.class})
class TypoReportToTypoMapperTest {
    @Autowired
    private TypoMapper typoMapper;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoReport")
    void requestReportToTypo(final TypoReport report) {
        final var typo = typoMapper.toTypo(report);
        assertThat(typo).isEqualsToTypoReport(report);
    }
}

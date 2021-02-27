package io.hexlet.typoreporter.test.utils;

import io.hexlet.typoreporter.domain.Identifiable;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class EntitiesFactory {

    public static Stream<Typo> getTypos() {
        final var typo1 = new Typo()
                .setId(1L)
                .setPageUrl("http://site.com")
                .setReporterName("ReporterName")
                .setReporterComment("ReporterComment")
                .setTextBeforeTypo("TextBeforeTypo")
                .setTextTypo("TextTypo")
                .setTextAfterTypo("TextAfterTypo");
        ReflectionTestUtils.setField(typo1, "createdBy", "system");
        ReflectionTestUtils.setField(typo1, "createdDate", LocalDateTime.now());
        ReflectionTestUtils.setField(typo1, "modifiedBy", "system");
        ReflectionTestUtils.setField(typo1, "modifiedDate", LocalDateTime.now());

        final var typo2 = new Typo()
                .setId(2L)
                .setPageUrl("http://site.com")
                .setReporterName("ReporterName")
                .setTextTypo("");
        return Stream.of(typo1, typo2);
    }

    public static Stream<TypoReport> getTypoReport() {
        final var typoReport = new TypoReport(
                "http://site.com",
                "ReporterName",
                "ReporterComment",
                "TextBeforeTypo",
                "TextTypo",
                "TextAfterTypo"
        );
        final var typoReportEmpty = new TypoReport(
                "http://site.com",
                "ReporterName",
                "ReporterComment",
                "",
                "TextTypo",
                ""
        );
        final var typoReportNull = new TypoReport(
                "http://site.com",
                "ReporterName",
                null,
                null,
                "TextTypo",
                null
        );
        return Stream.of(typoReport, typoReportEmpty, typoReportNull);
    }

    public static Stream<? extends Identifiable<Long>> getEntities() {
        return getTypos();
    }

    public static Stream<Long> getTypoIdsExist() {
        return Stream.of(1L, 5L, 10L);
    }

    public static Stream<Long> getTypoIdsNotExist() {
        return Stream.of(11L, 12L, 13L);
    }
}

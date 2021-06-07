package io.hexlet.typoreporter.service.dto.typo;

import io.hexlet.typoreporter.domain.typo.constraint.*;

public record TypoReport(
    @TypoPageUrl
    String pageUrl,
    @ReporterName
    String reporterName,
    @ReporterComment
    String reporterComment,
    @TextBeforeTypo
    String textBeforeTypo,
    @TextTypo
    String textTypo,
    @TextAfterTypo
    String textAfterTypo
) {

}

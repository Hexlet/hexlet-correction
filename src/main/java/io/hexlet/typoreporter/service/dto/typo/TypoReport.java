package io.hexlet.typoreporter.service.dto.typo;

import io.hexlet.typoreporter.domain.typo.constraint.ReporterComment;
import io.hexlet.typoreporter.domain.typo.constraint.ReporterName;
import io.hexlet.typoreporter.domain.typo.constraint.TextAfterTypo;
import io.hexlet.typoreporter.domain.typo.constraint.TextBeforeTypo;
import io.hexlet.typoreporter.domain.typo.constraint.TextTypo;
import io.hexlet.typoreporter.domain.typo.constraint.TypoPageUrl;

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

    public static TypoReport empty() {
        return new TypoReport("", "", "", "", "", "");
    }
}

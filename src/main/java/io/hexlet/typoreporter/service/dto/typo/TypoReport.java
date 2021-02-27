package io.hexlet.typoreporter.service.dto.typo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.hexlet.typoreporter.domain.typo.constraint.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

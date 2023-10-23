package io.hexlet.typoreporter.service.dto.typo;

import java.time.Instant;

public record ReportedTypo(
    Long id,
    String pageUrl,
    String reporterName,
    String reporterComment,
    String textBeforeTypo,
    String textTypo,
    String textAfterTypo,
    String createdBy,
    Instant createdDate
) {

}

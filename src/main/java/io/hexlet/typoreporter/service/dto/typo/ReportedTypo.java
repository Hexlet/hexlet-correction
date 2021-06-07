package io.hexlet.typoreporter.service.dto.typo;

import java.time.LocalDateTime;

public record ReportedTypo(
    Long id,
    String pageUrl,
    String reporterName,
    String reporterComment,
    String textBeforeTypo,
    String textTypo,
    String textAfterTypo,
    String createdBy,
    LocalDateTime createdDate
) {

}

package io.hexlet.typoreporter.service.dto.typo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

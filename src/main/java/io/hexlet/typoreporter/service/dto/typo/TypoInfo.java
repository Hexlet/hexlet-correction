package io.hexlet.typoreporter.service.dto.typo;

import io.hexlet.typoreporter.domain.typo.TypoStatus;

import java.time.LocalDateTime;

public record TypoInfo(
    Long id,
    String pageUrl,
    String reporterName,
    String reporterComment,
    String textBeforeTypo,
    String textTypo,
    String textAfterTypo,
    String typoStatusStr,
    TypoStatus typoStatus,
    String createdBy,
    String createdDateAgo,
    LocalDateTime createdDate,
    String modifiedBy,
    String modifiedDateAgo,
    LocalDateTime modifiedDate
) {

}

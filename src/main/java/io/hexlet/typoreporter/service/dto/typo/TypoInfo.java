package io.hexlet.typoreporter.service.dto.typo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.hexlet.typoreporter.domain.typo.TypoStatus;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

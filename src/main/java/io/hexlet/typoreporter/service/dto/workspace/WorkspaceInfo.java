package io.hexlet.typoreporter.service.dto.workspace;

import java.time.LocalDateTime;

public record WorkspaceInfo(
    Long id,
    String name,
    String description,
    String createdBy,
    LocalDateTime createdDate,
    String createdDateAgo,
    String modifiedBy,
    LocalDateTime modifiedDate,
    String modifiedDateAgo
) {

}

package io.hexlet.typoreporter.service.dto.workspace;


import java.time.Instant;

public record WorkspaceInfo(
    Long id,
    String name,
    String url,
    String description,
    String createdBy,
    Instant createdDate,
    String createdDateAgo,
    String modifiedBy,
    Instant modifiedDate,
    String modifiedDateAgo
) {

}

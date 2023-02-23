package io.hexlet.typoreporter.service.dto.workspace;

import io.hexlet.typoreporter.domain.workspace.Workspace;

public record WorkspaceRoleInfo (
    Long workspaceId,
    String workspaceName,
    String workspaceDescription,
    String accountId,
    String accountName,
    String roleName
) {
}

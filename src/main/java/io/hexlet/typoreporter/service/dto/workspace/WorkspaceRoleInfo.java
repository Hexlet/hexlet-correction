package io.hexlet.typoreporter.service.dto.workspace;

import io.hexlet.typoreporter.domain.workspace.Workspace;

public record WorkspaceRoleInfo (
    Long workspaceId,
    String workspaceName,
    String workspaceDescription,
    Long accountId,
    String accountName,
    String roleName
) {
}

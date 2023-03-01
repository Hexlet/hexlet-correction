package io.hexlet.typoreporter.service.dto.workspace;

import io.hexlet.typoreporter.domain.workspace.AccountRole;

public record WorkspaceRoleInfo (
    Long workspaceId,
    Long accountId,
    String workspaceName,
    String workspaceUrl,
    String workspaceDescription,
    String username,
    AccountRole role
) {
}

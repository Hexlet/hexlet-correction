package io.hexlet.typoreporter.service.dto.workspace;

import io.hexlet.typoreporter.domain.workspace.constraint.*;

public record CreateWorkspace(
    @WorkspaceName String name,
    @WorkspaceDescription String description
) {

}

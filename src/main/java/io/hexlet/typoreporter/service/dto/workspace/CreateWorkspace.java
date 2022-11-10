package io.hexlet.typoreporter.service.dto.workspace;

import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceDescription;
import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceName;
import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceUrl;

public record CreateWorkspace(
    @WorkspaceName String name,
    @WorkspaceDescription String description,
    @WorkspaceUrl String url
) {

}

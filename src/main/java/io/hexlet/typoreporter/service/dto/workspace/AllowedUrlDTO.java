package io.hexlet.typoreporter.service.dto.workspace;

import io.hexlet.typoreporter.domain.workspace.constraint.WorkspaceUrl;

public record AllowedUrlDTO(@WorkspaceUrl String url) {
}

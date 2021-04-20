package io.hexlet.typoreporter.service.dto.workspace;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.hexlet.typoreporter.domain.workspace.constraint.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record CreateWorkspace(@WorkspaceName String name, @WorkspaceDescription String description) {

}

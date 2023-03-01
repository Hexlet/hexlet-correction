package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceRoleInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WorkspaceRoleMapper {

    @Mapping(target = "workspaceId", source = "workspace.id")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "workspaceUrl", source = "workspace.url")
    @Mapping(target = "workspaceDescription", source = "workspace.description")
    @Mapping(target = "workspaceName", source = "workspace.name")
    @Mapping(target = "username", source = "account.username")
    WorkspaceRoleInfo toWorkspaceRoleInfo(WorkspaceRole source);
}

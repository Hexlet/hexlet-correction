package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceRoleInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface WorkspaceRoleMapper {

    @Mapping(target = "workspaceId", source = "workspace.id")
    @Mapping(target = "workspaceDescription", source = "workspace.description")
    @Mapping(target = "workspaceName", source = "workspace.name")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountName", source = "account.username")
    @Mapping(target = "roleName", source = "role")
    WorkspaceRoleInfo toWorkspaceRoleInfo(WorkspaceRole source);
}

package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = PrettyDateMapper.class)
public interface WorkspaceMapper {

    Workspace toWorkspace(CreateWorkspace source);

    @Mapping(target = "createdDateAgo", source = "createdDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "modifiedDateAgo", source = "modifiedDate", qualifiedByName = "mapToPrettyDateAgo")
    WorkspaceInfo toWorkspaceInfo(Workspace source);
}

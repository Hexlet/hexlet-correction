package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;

@Mapper
public interface WorkspaceMapper {
    CreateWorkspace toCreateWorkspace(Workspace source);

    Workspace toWorkspace(CreateWorkspace source);
    Workspace toWorkspace(WorkspaceInfo source);

    @Mapping(target = "createdDateAgo", source = "createdDate")
    @Mapping(target = "modifiedDateAgo", source = "modifiedDate")
    WorkspaceInfo toWorkspaceInfo(Workspace source);

    default String getDateAgoAsString(LocalDateTime date) {
        final PrettyTime pt = new PrettyTime();
        return pt.format(date);
    }

}



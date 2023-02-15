package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;

@Mapper
public interface WorkspaceMapper {

    PrettyTime prettyTime = new PrettyTime();

    Workspace toWorkspace(CreateWorkspace source);

    @Mapping(target = "createdDateAgo", source = "createdDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "modifiedDateAgo", source = "modifiedDate", qualifiedByName = "mapToPrettyDateAgo")
    WorkspaceInfo toWorkspaceInfo(Workspace source);

    @Named(value = "mapToPrettyDateAgo")
    default String getDateAgoAsString(LocalDateTime date) {
        return prettyTime.format(date);
    }
}

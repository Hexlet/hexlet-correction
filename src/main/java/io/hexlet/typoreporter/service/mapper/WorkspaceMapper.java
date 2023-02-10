package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {AccountMapper.class, TypoMapper.class})
public interface WorkspaceMapper {
    CreateWorkspace toCreateWorkspace(Workspace source);
    Workspace toWorkspace(CreateWorkspace source);
    Workspace toWorkspace(WorkspaceInfo source);

    @Mapping(source = "createdDate", target = "createdDateAgo", qualifiedBy = GetDateAsString.class)
    @Mapping(source = "modifiedDate", target = "modifiedDateAgo", qualifiedBy = GetDateAsString.class)
    WorkspaceInfo toWorkspaceInfo(Workspace source);

    @GetDateAsString(getDateAsString = "")
    static String getCreatedDateAgo(LocalDateTime date) {
        final PrettyTime pt = new PrettyTime();
        return pt.format(date);
    }

}



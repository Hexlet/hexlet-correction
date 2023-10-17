package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.Instant;

@Mapper
public interface WorkspaceMapper {

    PrettyTime prettyTime = new PrettyTime();

    Workspace toWorkspace(CreateWorkspace source);

    @Mapping(target = "createdDateAgo", source = "createdDate", qualifiedByName = "mapToPrettyDateAgo")
    @Mapping(target = "modifiedDateAgo", source = "modifiedDate", qualifiedByName = "mapToPrettyDateAgo")
    WorkspaceInfo toWorkspaceInfo(Workspace source);

    @Named(value = "mapToPrettyDateAgo")
    default String getDateAgoAsString(Instant date) {
        prettyTime.setLocale(LocaleContextHolder.getLocale());
        return prettyTime.format(date);
    }
}

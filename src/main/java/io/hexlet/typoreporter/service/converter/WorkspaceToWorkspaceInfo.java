package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Component
public class WorkspaceToWorkspaceInfo implements Converter<Workspace, WorkspaceInfo> {

    /**
     * Convert the source object of type {@code Workspace} to target type {@code WorkspaceInfo}.
     *
     * @param source the source object to convert, which must be an instance of {@code Workspace} (never {@code null})
     * @return the converted object, which must be an instance of {@code WorkspaceInfo} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public WorkspaceInfo convert(Workspace source) {
        return new WorkspaceInfo(
            source.getId(),
            source.getName(),
            source.getUrl(),
            source.getDescription(),
            source.getCreatedBy(),
            source.getCreatedDate(),
            source.getCreatedDate().format(ISO_LOCAL_DATE_TIME),
            source.getModifiedBy(),
            source.getModifiedDate(),
            source.getModifiedDate().format(ISO_LOCAL_DATE_TIME)
        );
    }
}

package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapper;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class WorkspaceToWorkspaceInfo implements Converter<Workspace, WorkspaceInfo> {

    /**
     * Convert the source object of type {@code Workspace} to target type {@code WorkspaceInfo}.
     *
     * @param source the source object to convert, which must be an instance of {@code Workspace} (never {@code null})
     * @return the converted object, which must be an instance of {@code WorkspaceInfo} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */

    private final WorkspaceMapper workspaceMapper = new WorkspaceMapperImpl();
    @Override
    public WorkspaceInfo convert(Workspace source) {
        return workspaceMapper.toWorkspaceInfo(source);
    }
}

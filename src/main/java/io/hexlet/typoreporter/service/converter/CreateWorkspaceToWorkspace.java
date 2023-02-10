package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapper;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapperImpl;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateWorkspaceToWorkspace implements Converter<CreateWorkspace, Workspace> {

    /**
     * Convert the source object of type {@code CreateWorkspace} to target type {@code Workspace}.
     *
     * @param source the source object to convert,
     *               which must be an instance of {@code CreateWorkspace} (never {@code null})
     * @return the converted object, which must be an instance of {@code Workspace} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */

    private final WorkspaceMapper workspaceMapper = new WorkspaceMapperImpl();
    @Override
    public Workspace convert(CreateWorkspace source) {
        return workspaceMapper.toWorkspace(source);
    }
}

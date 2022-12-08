package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class CreateWorkspaceToWorkspaceTest {

    private final WorkspaceMapper workspaceMapper = Mappers.getMapper(WorkspaceMapper.class);

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getCreateWorkspaces")
    void requestReportToTypo(final CreateWorkspace createWorkspace) {
        final var workspace = workspaceMapper.toWorkspace(createWorkspace);
        assertThat(workspace).usingRecursiveComparison()
            .ignoringFields("id", "typos", "createdDate", "createdBy", "modifiedDate", "modifiedBy",
                "workspaceRoles", "accounts", "workspaceSettings")
            .isEqualTo(createWorkspace);
    }
}

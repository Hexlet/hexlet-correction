package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {WorkspaceMapperImpl.class})
class CreateWorkspaceToWorkspaceTest {
    @Autowired
    private WorkspaceMapper workspaceMapper;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getCreateWorkspaces")
    void requestReportToTypo(final CreateWorkspace createWorkspace) {
        final var workspace = workspaceMapper.toWorkspace(createWorkspace);
        assertThat(workspace).usingRecursiveComparison()
            .ignoringFields("id", "apiAccessToken", "typos", "createdDate", "createdBy", "modifiedDate", "modifiedBy", "accounts")
            .isEqualTo(createWorkspace);
    }
}

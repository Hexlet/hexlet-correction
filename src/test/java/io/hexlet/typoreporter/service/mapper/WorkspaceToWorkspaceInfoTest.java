package io.hexlet.typoreporter.service.mapper;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {WorkspaceMapperImpl.class})
class WorkspaceToWorkspaceInfoTest {
    @Autowired
    private WorkspaceMapper workspaceMapper;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaces")
    void requestWorkspaceToInfo(final Workspace workspace) {
        final var workspaceInfo = workspaceMapper.toWorkspaceInfo(workspace);
        assertThat(workspaceInfo).usingRecursiveComparison()
            .ignoringFields("createdBy", "createdDate", "createdDateAgo", "modifiedBy", "modifiedDate", "modifiedDateAgo")
            .isEqualTo(workspace);
    }
}

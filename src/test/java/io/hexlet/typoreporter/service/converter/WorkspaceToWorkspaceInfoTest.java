package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.converter.Converter;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkspaceToWorkspaceInfoTest {
    private final Converter<Workspace, WorkspaceInfo> converter = new WorkspaceToWorkspaceInfo();

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaces")
    void requestWorkspaceToInfo(final Workspace workspace) {
        final var workspaceInfo = converter.convert(workspace);
        assertThat(workspaceInfo).usingRecursiveComparison()
            .ignoringFields("createdBy", "createdDate", "createdDateAgo", "modifiedBy", "modifiedDate", "modifiedDateAgo")
            .isEqualTo(workspace);
    }
}

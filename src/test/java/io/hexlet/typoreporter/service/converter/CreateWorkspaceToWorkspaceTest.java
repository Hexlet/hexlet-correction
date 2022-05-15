package io.hexlet.typoreporter.service.converter;

import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.converter.Converter;

import static org.assertj.core.api.Assertions.assertThat;


class CreateWorkspaceToWorkspaceTest {

    private final Converter<CreateWorkspace, Workspace> converter = new CreateWorkspaceToWorkspace();

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getCreateWorkspaces")
    void requestReportToTypo(final CreateWorkspace createWorkspace) {
        final var workspace = converter.convert(createWorkspace);
        assertThat(workspace).usingRecursiveComparison()
            .ignoringFields("id", "apiAccessToken", "typos", "createdDate", "createdBy", "modifiedDate", "modifiedBy", "accounts")
            .isEqualTo(createWorkspace);
    }
}

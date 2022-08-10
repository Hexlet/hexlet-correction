package io.hexlet.typoreporter.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.util.Objects;
import java.util.UUID;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.WORKSPACE_101_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "typos.yml"})
public class WorkspaceServiceIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private WorkspaceRepository repository;

    @Autowired
    private WorkspaceService service;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaces")
    void getAllWorkspacesInfoIsSuccessful() {
        WorkspaceInfo workspaceInfo = service.getAllWorkspacesInfo().stream()
            .filter(wksInfo -> wksInfo.name().equals(WORKSPACE_101_NAME))
            .findFirst().orElse(null);

        assertThat(Objects.requireNonNull(workspaceInfo).createdDateAgo()).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaces")
    void getWorkspaceInfoByNameIsSuccessful() {
        WorkspaceInfo workspaceInfo = service.getWorkspaceInfoByName(WORKSPACE_101_NAME).orElse(null);
        assertThat(workspaceInfo != null).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void existsWorkspaceByNameIsSuccessful(final String wksName) {
        assertThat(service.existsWorkspaceByName(wksName)).isTrue();
    }

    @Test
    void createWorkspaceIsSuccessful() {
        final var newWks = new CreateWorkspace("wks-name-1", "wks desc");
        WorkspaceInfo workspaceInfo = service.createWorkspace(newWks);
        assertThat(workspaceInfo.name()).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getCreateWorkspaces")
    void createWorkspaceWithWorkspaceAlreadyExistException(CreateWorkspace createWks) {
        Throwable thrown = assertThrows(WorkspaceAlreadyExistException.class, () -> {
            service.createWorkspace(createWks);
        });
        assertNotNull(thrown.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void updateWorkspaceIsSuccessful() {
        final var newWks = new CreateWorkspace("wks-name-1", "wks desc");
        WorkspaceInfo workspaceInfo = service.updateWorkspace(newWks, "wks-test").orElse(null);
        assertThat(Objects.requireNonNull(workspaceInfo).name()).isEqualTo(newWks.name());
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void deleteWorkspaceByNameIsSuccessful(final String wksName) {
        final Integer successfulCode = 1;
        assertThat(service.deleteWorkspaceByName(wksName)).isEqualTo(successfulCode);
        assertThat(service.existsWorkspaceByName(wksName)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void getWorkspaceApiAccessTokenByNameIsSuccessful(final String wksName) {
        UUID uuid = service.getWorkspaceApiAccessTokenByName(wksName).orElse(null);
        assertThat(uuid != null).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void regenerateWorkspaceApiAccessTokenByNameIsSuccessful(final String wksName) {
        UUID previousUuid = service.getWorkspaceApiAccessTokenByName(wksName).orElse(null);
        UUID newUuid = service.regenerateWorkspaceApiAccessTokenByName(wksName).orElse(null);
        assertThat(previousUuid).isNotEqualTo(newUuid);
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void getWorkspaceByNameIsSuccessful(final String wksName) {
        Workspace workspace = service.getWorkspaceByName(wksName).orElse(null);
        assertThat(workspace != null).isTrue();
    }
}

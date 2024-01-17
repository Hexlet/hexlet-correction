package io.hexlet.typoreporter.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapper;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import io.hexlet.typoreporter.handler.exception.WorkspaceAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Objects;
import java.util.UUID;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.WORKSPACE_101_ID;
import static java.util.Objects.requireNonNull;
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
    private WorkspaceService service;

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Autowired
    private WorkspaceRepository workspaceRepository;

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
            .filter(wksInfo -> wksInfo.id().equals(WORKSPACE_101_ID))
            .findFirst().orElse(null);

        assertThat(Objects.requireNonNull(workspaceInfo).createdDateAgo()).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void getWorkspaceInfoByIdIsSuccessful(final Long wksId) {
        WorkspaceInfo workspaceInfo = service.getWorkspaceInfoById(wksId).orElse(null);
        assertThat(workspaceInfo != null).isTrue();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void existsWorkspaceByNameIsSuccessful(final Long wksId) {
        assertThat(service.existsWorkspaceById(wksId)).isTrue();
    }

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createWorkspaceIsSuccessful() {
        final var newWks = new CreateWorkspace("wks-name-1", "wks desc", "https://other.com");
        final var wksToCreate = requireNonNull(workspaceMapper.toWorkspace(newWks));
        final var wksSettings = new WorkspaceSettings();
        wksSettings.setWorkspace(wksToCreate);
        wksSettings.setApiAccessToken(UUID.randomUUID());
        WorkspaceInfo workspaceInfo = workspaceMapper.toWorkspaceInfo(workspaceRepository.save(wksToCreate));

        assertThat(workspaceInfo.name()).isNotEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getCreateWorkspaces")
    void createWorkspaceWithWorkspaceAlreadyExistException(CreateWorkspace createWks) {
        Throwable thrown = assertThrows(WorkspaceAlreadyExistException.class, () -> {
            service.createWorkspace(createWks, "user");
        });
        assertNotNull(thrown.getMessage());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void updateWorkspaceIsSuccessful() {
        final var newUrl = "https://updatemysite.com";
        assertThat(workspaceRepository.existsWorkspaceByUrl(newUrl)).isFalse();
        final var newWks = new CreateWorkspace("wks-name-1", "wks desc", newUrl);
        WorkspaceInfo workspaceInfo = service.updateWorkspace(newWks, WORKSPACE_101_ID);
        assertThat(Objects.requireNonNull(workspaceInfo).name()).isEqualTo(newWks.name());
        assertThat(workspaceInfo.url()).isEqualTo(newWks.url());
        assertThat(workspaceInfo.description()).isEqualTo(newWks.description());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void deleteWorkspaceByNameIsSuccessful(final Long wksId) {
        final Integer successfulCode = 1;
        assertThat(service.deleteWorkspaceById(wksId)).isEqualTo(successfulCode);
        assertThat(service.existsWorkspaceById(wksId)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void getWorkspaceByNameIsSuccessful(final Long wksId) {
        Workspace workspace = service.getWorkspaceById(wksId).orElse(null);
        assertThat(workspace != null).isTrue();
    }
}

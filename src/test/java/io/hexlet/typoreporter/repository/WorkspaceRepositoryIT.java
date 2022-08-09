package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.security.model.SecuredWorkspace;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.util.Optional;
import java.util.UUID;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(AuditConfiguration.class)
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "typos.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WorkspaceRepositoryIT {
    final Integer SUCCESSFUL_CODE = 1;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceByName(final String wksName) {
        final var wks = workspaceRepository.getWorkspaceByName(wksName);
        assertThat(wks).isNotEmpty();
        assertThat(wks.map(Workspace::getName).orElseThrow()).isEqualTo(wksName);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "wks-not-exists")
    void getWorkspaceByNameNotExist(final String wksName) {
        assertThat(workspaceRepository.getWorkspaceByName(wksName)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void existsWorkspaceByName(final String wksName) {
        assertThat(workspaceRepository.existsWorkspaceByName(wksName)).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "wks-not-exists")
    void existsWorkspaceByNameNotExist(final String wksName) {
        assertThat(workspaceRepository.existsWorkspaceByName(wksName)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void deleteWorkspaceByNameIsSuccessful(final String wksName) {
        assertThat(workspaceRepository.deleteWorkspaceByName(wksName)).isEqualTo(SUCCESSFUL_CODE);
        assertThat(workspaceRepository.existsWorkspaceByName(wksName)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void getSecuredWorkspaceByNameIsSuccessful(final String wksName) {
        Optional<SecuredWorkspace> securedWorkspace = workspaceRepository.getSecuredWorkspaceByName(wksName);
        String name = securedWorkspace.map(SecuredWorkspace::getUsername).orElse(null);

        assertThat(name).isEqualTo("wks-test");
        assertThat(workspaceRepository.existsWorkspaceByName(wksName)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = "wks-test")
    void updateApiAccessTokenByWorkspaceNameIsSuccessful(final String wksName) {
        final UUID newToken = UUID.randomUUID();
        assertThat(workspaceRepository.updateApiAccessTokenByWorkspaceName(wksName, newToken)).isEqualTo(SUCCESSFUL_CODE);

        String newUuid = workspaceRepository.getWorkspaceByName(wksName)
            .map(Workspace::getApiAccessToken)
            .map(Object::toString).orElse(null);
        assertThat(newUuid).isEqualTo(newToken.toString());
    }
}

package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.workspace.Workspace;
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
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void getWorkspaceById(final Long wksId) {
        final var wks = workspaceRepository.getWorkspaceById(wksId);
        assertThat(wks).isNotEmpty();
        assertThat(wks.map(Workspace::getId).orElseThrow()).isEqualTo(wksId);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {9999L, 8888L, 7777L})
    void getWorkspaceByIdNotExist(final Long wksId) {
        assertThat(workspaceRepository.getWorkspaceById(wksId)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void existsWorkspaceById(final Long wksId) {
        assertThat(workspaceRepository.existsWorkspaceById(wksId)).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {9999L, 8888L, 7777L})
    void existsWorkspaceByIdNotExist(final Long wksId) {
        assertThat(workspaceRepository.existsWorkspaceById(wksId)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void deleteWorkspaceByIdIsSuccessful(final Long wksId) {
        assertThat(workspaceRepository.deleteWorkspaceById(wksId)).isEqualTo(SUCCESSFUL_CODE);
        assertThat(workspaceRepository.existsWorkspaceById(wksId)).isFalse();
    }
}

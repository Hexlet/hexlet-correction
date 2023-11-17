package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.*;
import org.springframework.data.domain.*;
import org.springframework.test.context.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Testcontainers
@Import(AuditConfiguration.class)
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "typos.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TypoRepositoryIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private TypoRepository typoRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void notSaveTypoEntityWithInvalidWorkspaceId(final Typo typo) {
        final var newTypo = typo.setId(null);
        newTypo.getWorkspace().setId(999_999_999L);
        assertThrows(DataIntegrityViolationException.class, () -> typoRepository.saveAndFlush(newTypo));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypos")
    void notSaveTypoEntityWithNullWorkspaceId(final Typo typo) {
        final var newTypo = typo.setId(null);
        newTypo.getWorkspace().setId(null);
        assertThrows(InvalidDataAccessApiUsageException.class, () -> typoRepository.saveAndFlush(newTypo));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void findPageTypoByWorkspaceName(final Long wksId) {
        final var wks = workspaceRepository.findById(wksId).orElseThrow();
        final var pageReq = PageRequest.of(0, 10, Sort.by("createdDate"));
        final var typoPage = typoRepository.findPageTypoByWorkspaceId(pageReq, wksId);
        assertThat(typoPage.getTotalElements()).isEqualTo(wks.getTypos().size());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {9999L, 8888L, 7777L})
    void findPageTypoByWorkspaceNameNotExist(final Long wksId) {
        final var pageReq = PageRequest.of(0, 10, Sort.by("createdDate"));
        final var typoPage = typoRepository.findPageTypoByWorkspaceId(pageReq, wksId);
        assertThat(typoPage.getTotalElements()).isZero();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoIdsExist")
    void deleteTypoById(final Long id) {
        assertThat(typoRepository.existsById(id)).isTrue();
        assertThat(typoRepository.deleteTypoById(id)).isNotZero();
        assertThat(typoRepository.existsById(id)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoIdsNotExist")
    void deleteTypoByIdNotFound(final Long id) {
        assertThat(typoRepository.existsById(id)).isFalse();
        assertThat(typoRepository.deleteTypoById(id)).isZero();
        assertThat(typoRepository.existsById(id)).isFalse();
    }
}

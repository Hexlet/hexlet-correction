package io.hexlet.typoreporter.service;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.service.dto.typo.*;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import io.hexlet.typoreporter.test.asserts.ReportedTypoAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.TypoReporterApplicationIT.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.*;
import static io.hexlet.typoreporter.domain.typo.TypoStatus.*;
import static io.hexlet.typoreporter.test.utils.EntitiesFactory.WORKSPACE_101_NAME;
import static io.hexlet.typoreporter.web.Routers.DEFAULT_SORT_FIELD;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "typos.yml"})
public class TypoServiceIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private TypoRepository repository;

    @Autowired
    private TypoService service;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoReport")
    void addTypoReport(final TypoReport report) {
        final var reportedTypo = service.addTypoReport(report, WORKSPACE_101_NAME);

        ReportedTypoAssert.assertThat(reportedTypo).isEqualsToTypoReport(report);
        assertThat(repository.existsById(reportedTypo.id())).isTrue();
    }

    @Test
    void getPageTypo() {
        final var totalSize = repository.findAll()
            .stream()
            .map(Typo::getWorkspace)
            .map(Workspace::getName)
            .filter(WORKSPACE_101_NAME::equals)
            .count();
        final var page = 1;
        final var pageSize = 3;
        final var pageReq = PageRequest.of(page, pageSize, Sort.by(DEFAULT_SORT_FIELD));
        final var pageTypo = service.getTypoPage(pageReq, WORKSPACE_101_NAME);
        assertThat(pageTypo.getTotalPages()).isEqualTo(totalSize / pageSize + page);
        assertThat(pageTypo.getTotalElements()).isEqualTo(totalSize);
        assertThat(pageTypo.getSize()).isEqualTo(pageSize);
        assertThat(pageTypo.getNumber()).isEqualTo(page);
        assertThat(pageTypo.getNumberOfElements()).isEqualTo(pageSize);
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoWithEventNullValues(final Long id) {
        final var typoStatus = repository.findById(id).orElseThrow().getTypoStatus();
        final var newTypoStatus = service.updateTypoStatus(id, null).orElseThrow().typoStatus();
        assertThat(newTypoStatus).isEqualTo(typoStatus);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoEventResolveToResolved(final Long id) {
        final var typoNotInProgress = repository.findById(id)
            .map(Typo::getTypoStatus)
            .filter(not(IN_PROGRESS::equals))
            .isPresent();
        if (typoNotInProgress) {
            return;
        }
        final var patchedTypo = service.updateTypoStatus(id, RESOLVE).orElseThrow();

        assertThat(patchedTypo.typoStatus()).isEqualTo(RESOLVED);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoEventOpenToReported(final Long id) {
        final var typoNotReported = repository.findById(id)
            .map(Typo::getTypoStatus)
            .filter(not(REPORTED::equals))
            .isPresent();
        if (typoNotReported) {
            return;
        }
        final var typoStatus = service.updateTypoStatus(id, OPEN)
            .map(TypoInfo::typoStatus)
            .orElseThrow();
        assertThat(typoStatus).isEqualTo(IN_PROGRESS);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoEventReopenToCanceled(final Long id) {
        final var typoNotCanceled = repository.findById(id)
            .map(Typo::getTypoStatus)
            .filter(not(CANCELED::equals))
            .isPresent();
        if (typoNotCanceled) {
            return;
        }
        final var typoStatus = service.updateTypoStatus(id, REOPEN)
            .map(TypoInfo::typoStatus)
            .orElseThrow();

        assertThat(typoStatus).isEqualTo(IN_PROGRESS);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void deleteTypoById(final Long id) {
        assertThat(repository.existsById(id)).isTrue();
        assertThat(service.deleteTypoById(id)).isNotZero();
        assertThat(repository.existsById(id)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsNotExist")
    void deleteTypoByIdNotFound(final Long id) {
        assertThat(repository.existsById(id)).isFalse();
        assertThat(service.deleteTypoById(id)).isZero();
        assertThat(repository.existsById(id)).isFalse();
    }
}

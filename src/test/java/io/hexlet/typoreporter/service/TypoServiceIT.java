package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.service.dto.typo.*;
import io.hexlet.typoreporter.test.asserts.ReportedTypoAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import static io.hexlet.typoreporter.TypoReporterApplicationIT.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.*;
import static io.hexlet.typoreporter.domain.typo.TypoStatus.*;
import static io.hexlet.typoreporter.web.Routers.TYPO_SORT;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Testcontainers
@Transactional
@Sql("classpath:db/test-data/CREATE_TYPOS.sql")
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
        final var reportedTypo = service.addTypoReport(report);

        ReportedTypoAssert.assertThat(reportedTypo).isEqualsToTypoReport(report);
        assertThat(repository.existsById(reportedTypo.id())).isTrue();
    }

    @Test
    void getPageTypo() {
        final var totalSize = repository.count();
        final var page = 1;
        final var pageSize = 3;
        final var pageReq = PageRequest.of(page, pageSize, Sort.by(TYPO_SORT));
        final var pageTypo = service.getTypoPage(pageReq);
        assertThat(pageTypo.getTotalPages()).isEqualTo(totalSize / pageSize + page);
        assertThat(pageTypo.getTotalElements()).isEqualTo(totalSize);
        assertThat(pageTypo.getSize()).isEqualTo(pageSize);
        assertThat(pageTypo.getNumber()).isEqualTo(page);
        assertThat(pageTypo.getNumberOfElements()).isEqualTo(pageSize);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void getTypoById(final Long id) {
        assertThat(service.getTypoById(id).map(Typo::getId)).isNotEmpty().hasValue(id);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsNotExist")
    void getTypoByIdNotFound(final Long id) {
        assertThat(service.getTypoById(id)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoReporterComment(final Long id) {
        final var typo = repository.findById(id).orElseThrow();
        final var newComment = typo.getReporterComment() + " <- new comment";
        final var patchTypo = new PatchTypo(newComment, null);
        final var patchedTypo = service.patchTypoById(id, patchTypo).orElseThrow();

        assertThat(patchedTypo.getId()).isEqualTo(id);
        assertThat(patchedTypo.getReporterComment()).isEqualTo(newComment);
        assertThat(patchedTypo.getTypoStatus()).isEqualTo(typo.getTypoStatus());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoWithNullValues(final Long id) {
        final var typo = repository.findById(id).orElseThrow();
        final var patchTypo = new PatchTypo(null, null);
        final var patchedTypo = service.patchTypoById(id, patchTypo).orElseThrow();

        assertThat(patchedTypo.getId()).isEqualTo(id);
        assertThat(patchedTypo.getReporterComment()).isEqualTo(null);
        assertThat(patchedTypo.getTypoStatus()).isEqualTo(typo.getTypoStatus());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoEventResolveToResolved(final Long id) {
        final var typo = repository.findById(id)
                .map(t -> t.setTypoStatus(t.getTypoStatus().next(OPEN)))
                .map(repository::save)
                .orElseThrow();
        final var patchTypo = new PatchTypo(typo.getReporterComment(), RESOLVE);
        final var patchedTypo = service.patchTypoById(id, patchTypo).orElseThrow();

        assertThat(patchedTypo.getId()).isEqualTo(id);
        assertThat(patchedTypo.getReporterComment()).isEqualTo(typo.getReporterComment());
        assertThat(patchedTypo.getTypoStatus()).isEqualTo(RESOLVED);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoEventOpenToInProgress(final Long id) {
        final var typo = repository.findById(id).orElseThrow();
        final var patchTypo = new PatchTypo(typo.getReporterComment(), OPEN);
        final var patchedTypo = service.patchTypoById(id, patchTypo).orElseThrow();

        assertThat(patchedTypo.getId()).isEqualTo(id);
        assertThat(patchedTypo.getReporterComment()).isEqualTo(typo.getReporterComment());
        assertThat(patchedTypo.getTypoStatus()).isEqualTo(IN_PROGRESS);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void patchTypoEventCancelToCanceled(final Long id) {
        final var typo = repository.findById(id).orElseThrow();
        final var patchTypo = new PatchTypo(typo.getReporterComment(), CANCEL);
        final var patchedTypo = service.patchTypoById(id, patchTypo).orElseThrow();

        assertThat(patchedTypo.getId()).isEqualTo(id);
        assertThat(patchedTypo.getReporterComment()).isEqualTo(typo.getReporterComment());
        assertThat(patchedTypo.getTypoStatus()).isEqualTo(CANCELED);
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

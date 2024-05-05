package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.workspace.AllowedUrl;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
@DataSet(value = {"workspaces.yml", "allowedUrls.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AllowedUrlRepositoryIT {
    final Integer SUCCESSFUL_CODE = 1;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private AllowedUrlRepository allowedUrlRepository;
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceAndAllowedUrlsRelated")
    void getAllowedUrlByUrl(final Long wksId, final String url) {
        final var allowedUrl = allowedUrlRepository.findAllowedUrlByUrlAndWorkspaceId(url, wksId);
        assertThat(allowedUrl).isNotEmpty();
        assertThat(allowedUrl.map(AllowedUrl::getUrl).orElseThrow()).isEqualTo(url);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceAndAllowedUrlsNotRelated")
    void getAllowedUrlByUrlNotExists(final Long wksId, final String url) {
        assertThat(allowedUrlRepository.findAllowedUrlByUrlAndWorkspaceId(url, wksId)).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getAllowedUrls")
    void notSaveAllowedUrlEntityWithInvalidWorkspaceId(final AllowedUrl allowedUrl) {
        final var newUrl = allowedUrl.setId(null);
        newUrl.getWorkspace().setId(999_999_999L);
        assertThrows(DataIntegrityViolationException.class, () -> allowedUrlRepository.saveAndFlush(newUrl));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getAllowedUrls")
    void notSaveAllowedUrlEntityWithNullWorkspace(final AllowedUrl allowedUrl) {
        final var newUrl = allowedUrl.setId(null);
        newUrl.setWorkspace(null);
        assertThrows(ConstraintViolationException.class, () -> allowedUrlRepository.saveAndFlush(newUrl));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceIdsExist")
    void findPageAllowedUrlByWorkspaceId(final Long wksId) {
        final var wks = workspaceRepository.getWorkspaceById(wksId).orElseThrow();
        final var pageReq = PageRequest.of(0, 10, Sort.by("url"));
        final var urlPage = allowedUrlRepository.findPageAllowedUrlByWorkspaceId(pageReq, wksId);
        assertThat(urlPage.getTotalElements()).isEqualTo(wks.getAllowedUrls().size());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {9999L, 8888L, 7777L})
    void findPageAllowedUrlByWorkspaceIdNotExist(final Long wksId) {
        final var pageReq = PageRequest.of(0, 10, Sort.by("url"));
        final var urlPage = allowedUrlRepository.findPageAllowedUrlByWorkspaceId(pageReq, wksId);
        assertThat(urlPage.getTotalElements()).isZero();
    }
}

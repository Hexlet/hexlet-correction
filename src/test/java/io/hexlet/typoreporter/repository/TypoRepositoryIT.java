package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.*;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.TypoReporterApplicationIT.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(AuditConfiguration.class)
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = "typos.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TypoRepositoryIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private TypoRepository repository;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypos")
    void saveTypoEntity(final Typo typo) {
        final var id = repository.save(typo.setId(null)).getId();
        assertThat(repository.existsById(id)).isTrue();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
    void deleteTypoById(final Long id) {
        assertThat(repository.existsById(id)).isTrue();
        assertThat(repository.deleteTypoById(id)).isNotZero();
        assertThat(repository.existsById(id)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsNotExist")
    void deleteTypoByIdNotFound(final Long id) {
        assertThat(repository.existsById(id)).isFalse();
        assertThat(repository.deleteTypoById(id)).isZero();
        assertThat(repository.existsById(id)).isFalse();
    }
}

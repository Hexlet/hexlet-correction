package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@DataSet(value = {"accounts.yml", "workspaces.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountRepositoryIT {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getAccountEmailExist")
    void getAccountByEmail(final String email) {
        final var account = accountRepository.findAccountByEmail(email);
        assertThat(account).isNotEmpty();
        assertThat(account.map(Account::getEmail).orElseThrow()).isEqualTo(email);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "invalid-email")
    void getAccountByEmailNotExist(final String email) {
        assertThat(accountRepository.findAccountByEmail(email)).isEmpty();
    }

    @Test
    void getPageAccountByWorkspaceName() {
        Workspace wks = workspaceRepository.findAll().stream().findFirst().get();
        accountRepository.findAll().forEach(account -> account.setWorkspace(wks));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Account> page = accountRepository.findPageAccountByWorkspaceName(pageable, wks.getName());
        assertThat(page).isNotEmpty();
        assertThat(page.getTotalElements()).isEqualTo(accountRepository.count());
        assertThat(page.getTotalPages()).isEqualTo(1);
    }
}

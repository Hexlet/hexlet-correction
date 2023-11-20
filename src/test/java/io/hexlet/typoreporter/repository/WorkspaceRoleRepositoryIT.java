package io.hexlet.typoreporter.repository;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.config.audit.AuditConfiguration;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@Import(AuditConfiguration.class)
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"accounts.yml", "workspaces.yml", "workspaceRoles.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WorkspaceRoleRepositoryIT {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private WorkspaceRoleRepository workspaceRoleRepository;
    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getAccountsIdExist")
    void getWorkspaceRolesByAccountIdIsSuccessful(final Long accountId) {
        List<WorkspaceRole> workspaceRoles = workspaceRoleRepository.getWorkspaceRolesByAccountId(accountId);

        assertThat(workspaceRoles).isNotEmpty();
        assertThat(workspaceRoles.get(0).getAccount().getId()).isEqualTo(accountId);
    }

    //my add
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getAccountUsernameExist")
//    void getWorkspaceRolesByAccountIdIsSuccessful(final String username) {
//        List<WorkspaceRole> workspaceRoles = workspaceRoleRepository.getWorkspaceRolesByAccountUsername(username);
//
//        assertThat(workspaceRoles).isNotEmpty();
//        assertThat(workspaceRoles.get(0).getAccount().getUsername()).isEqualTo(username);
//    }
    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getAccountEmailExist")
    void getWorkspaceRolesByAccountIdIsSuccessful(final String email) {
        List<WorkspaceRole> workspaceRoles = workspaceRoleRepository.getWorkspaceRolesByAccountEmail(email);

        assertThat(workspaceRoles).isNotEmpty();
        assertThat(workspaceRoles.get(0).getAccount().getEmail()).isEqualTo(email);
    }
    //my add end

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesIdExist")
    void getWorkspaceRolesByWorkspaceIdIsSuccessful(final Long workspaceId) {
        List<WorkspaceRole> workspaceRoles = workspaceRoleRepository.getWorkspaceRolesByWorkspaceId(workspaceId);

        assertThat(workspaceRoles).isNotEmpty();
        assertThat(workspaceRoles.get(0).getWorkspace().getId()).isEqualTo(workspaceId);
    }
}

package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRoleId;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.mapper.WorkspaceMapper;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.WorkspaceRoleService;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@Testcontainers
@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml"})
class WorkspaceControllerIT {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private WorkspaceRepository repository;

    @Autowired
    private WorkspaceService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WorkspaceRoleService workspaceRoleService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceInfoPageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksName}", wksName))
            .andExpect(model().attributeExists("wksInfo", "wksName"))
            .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains(wksName, workspace.getCreatedBy());
    }

    @Test
    void getWorkspaceInfoPageWithoutWks() throws Exception {
        mockMvc.perform(get("/workspace/{wksName}", "notExistsWksName"))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @Test
    void getWorkspaceSettingsPageWithoutWks() throws Exception {
        mockMvc.perform(get("/workspace/{wksName}/settings", "notExistsWksName"))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceTyposPageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksName}/typos", wksName))
            .andExpect(model().attributeExists("wksInfo", "wksName", "typoPage", "availableSizes", "sortProp", "sortDir", "DESC", "ASC"))
            .andReturn().getResponse();

        Typo typo = workspace.getTypos().stream()
            .sorted(Comparator.comparing(AbstractAuditingEntity::getCreatedDate))
            .findFirst().orElse(null);

        if (typo != null) {
            assertThat(response.getContentAsString()).contains(
                typo.getPageUrl(), typo.getReporterName(), typo.getModifiedBy()
            );
        }
    }

    @Test
    void getWorkspaceSettingsPageWithoutWksInfo() throws Exception {
        mockMvc.perform(get("/workspace/{wksName}/typos", "notExistsWksName"))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceUpdatePageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksName}/update", wksName))
            .andExpect(model().attributeExists("wksName", "formModified", "formModified"))
            .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains(workspace.getDescription());
    }

    @Test
    void getWorkspaceUpdatePageWithoutWks() throws Exception {
        mockMvc.perform(get("/workspace/{wksName}/update", "notExistsWksName"))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void putWorkspaceUpdateIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);
        String newWksName = "createWksName01";
        LocalDateTime previosModifiedDate = workspace.getModifiedDate();

        mockMvc.perform(put("/workspace/{wksName}/update", wksName)
                .param("name", newWksName)
                .param("url", "https://other.com")
                .param("description", "Wks description 01")
                .with(csrf()))
            .andExpect(redirectedUrl("/workspace/" + newWksName));

        LocalDateTime newModifiedDate = repository.getWorkspaceByName(newWksName).orElse(null).getModifiedDate();
        assertThat(previosModifiedDate).isNotEqualTo(newModifiedDate);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void putWorkspaceUpdateWithExistingWksUpdateName(final String wksName) throws Exception {
        String newWksName = "createWksName01";
        String wksDescription = "Wks description";
        String wksUrl = "https://other.com";

        LocalDateTime previosModifiedDate = repository.getWorkspaceByName(wksName).orElse(null).getModifiedDate();

        var createWks = new CreateWorkspace(newWksName, wksDescription, wksUrl);
        final var wksToCreate = requireNonNull(workspaceMapper.toWorkspace(createWks));
        final var wksSettings = new WorkspaceSettings();
        wksSettings.setWorkspace(wksToCreate);
        wksSettings.setApiAccessToken(UUID.randomUUID());
        workspaceRepository.save(wksToCreate);

        assertThat(repository.existsWorkspaceByName(newWksName)).isTrue();

        mockMvc.perform(put("/workspace/{wksName}/update", wksName)
                .param("name", newWksName)
                .param("description", wksDescription)
                .with(csrf()))
            .andExpect(model().attributeExists("createWorkspace"));

        LocalDateTime newModifiedDate = repository.getWorkspaceByName(wksName).orElse(null).getModifiedDate();
        assertThat(previosModifiedDate).isEqualTo(newModifiedDate);
    }

    //TODO tests for concurrency transactions in putWorkspaceUpdate() try-catch block

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void deleteWorkspaceByNameIsSuccessful(final String wksName) throws Exception {
        assertThat(repository.existsWorkspaceByName(wksName)).isTrue();

        MockHttpServletResponse response = mockMvc.perform(delete("/workspace/{wksName}", wksName)
                .with(csrf()))
            .andReturn().getResponse();

        assertThat(response.getRedirectedUrl()).isEqualTo("/workspaces");
        assertThat(repository.existsWorkspaceByName(wksName)).isFalse();
    }

    @Test
    void deleteWorkspaceByNameIsNotSuccessful() throws Exception {
        mockMvc.perform(delete("/workspace/{wksName}", "notExistsWksName").with(csrf()))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceUsersPage(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);
        Set<Account> accounts = accountRepository.findAll().stream().collect(Collectors.toSet());

        accounts.forEach(account -> {
            final var workspaceRoleId = new WorkspaceRoleId(workspace.getId(), account.getId());
            final var workspaceRole = new WorkspaceRole(workspaceRoleId, AccountRole.ROLE_ANONYMOUS, workspace, account);
            workspace.addWorkspaceRole(workspaceRole);
        });

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksName}/users", wksName))
                .andExpect(model().attributeExists("wksInfo", "wksName", "userPage", "availableSizes", "sortProp", "sortDir", "DESC", "ASC"))
                .andReturn().getResponse();

        for (WorkspaceRole workspaceRole : workspace.getWorkspaceRoles()) {
            assertThat(response.getContentAsString()).contains(
                    workspaceRole.getId().toString()
            );
        }
    }
}

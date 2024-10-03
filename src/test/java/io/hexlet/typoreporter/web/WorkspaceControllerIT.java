package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.typo.TypoStatus;
import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRoleId;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.AllowedUrlRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceRoleRepository;
import io.hexlet.typoreporter.service.dto.account.CustomUserDetails;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.*;

import org.junit.jupiter.api.Disabled;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;

import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.WorkspaceRoleService;

import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ACCOUNT_102_EMAIL;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ACCOUNT_102_ID;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ACCOUNT_103_EMAIL;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ACCOUNT_INCORRECT_EMAIL;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.WORKSPACE_103_ID;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE,
    dataTypeFactoryClass = DBUnitEnumPostgres.class,
    cacheConnection = false)
@DataSet(value = {"workspaces.yml", "workspaceRoles.yml", "accounts.yml", "typos.yml"})
class WorkspaceControllerIT {

    private static final Long NOT_EXISTING_WKS_ID = 9999L;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private WorkspaceRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AllowedUrlRepository allowedUrlRepository;

    @Autowired
    private WorkspaceRoleRepository workspaceRoleRepository;

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
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void getWorkspaceInfoPageIsSuccessful(final Long wksId,
                                          final String email) throws Exception {
        Workspace workspace = repository.getWorkspaceById(wksId).orElse(null);
        assertThat(workspace).isNotNull();

        MockHttpServletResponse response = mockMvc.perform(
                get("/workspace/{wksId}", wksId)
                    .with(user(new CustomUserDetails(email, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER"))))))
            .andExpect(model().attributeExists("wksInfo", "wksName"))
            .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains(workspace.getName(), workspace.getCreatedBy());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersNotRelated")
    void getWorkspaceInfoPageIsNotSuccessful(final Long wksId,
                                             final String email) throws Exception {
        mockMvc.perform(get("/workspace/{wksId}", wksId))
            .andExpect(redirectedUrl("/workspaces"));
        mockMvc.perform(get("/workspace/{wksId}", wksId).with(user(new CustomUserDetails(email, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER"))))))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @Test
    void getWorkspaceInfoPageWithoutWks() throws Exception {
        mockMvc.perform(get("/workspace/{wksId}", NOT_EXISTING_WKS_ID))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @Test
    void getWorkspaceSettingsPageWithoutWks() throws Exception {
        mockMvc.perform(get("/workspace/{wksId}/settings", NOT_EXISTING_WKS_ID))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void getWorkspaceTyposPageIsSuccessful(final Long wksId, final String email) throws Exception {
        Workspace workspace = repository.getWorkspaceById(wksId).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksId}/typos", wksId)
                .with(user(new CustomUserDetails(email, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER"))))))
            .andExpect(model().attributeExists("wksInfo", "wksName",
                "typoPage", "availableSizes", "sortProp", "sortDir", "DESC", "ASC"))
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

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersAndTypoStatusRelated")
    void getWorkspaceTyposPageFilteredIsSuccessful(final Long wksId, final String email,
                                                   final String typoStatus) throws Exception {
        Workspace workspace = repository.getWorkspaceById(wksId).orElse(null);

        var request = get("/workspace/{wksId}/typos", wksId).queryParam("typoStatus", typoStatus);

        MockHttpServletResponse response = mockMvc.perform(request
                .with(user(new CustomUserDetails(email, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER"))))))
            .andExpect(model().attributeExists("wksInfo", "wksName", "typoPage",
                "availableSizes", "sortProp", "sortDir", "DESC", "ASC", "typoStatus"))
            .andReturn().getResponse();

        Typo typo = workspace.getTypos().stream()
            .filter(t -> !t.getTypoStatus().equals(TypoStatus.valueOf(typoStatus)))
            .findFirst().orElse(null);

        if (typo != null) {
            assertThat(response.getContentAsString()).contains(
                typo.getPageUrl(), typo.getReporterName(), typo.getModifiedBy()
            );
        } else {
            fail("No typos without status " + typoStatus);
        }
    }

    @Test
    void getWorkspaceSettingsPageWithoutWksInfo() throws Exception {
        mockMvc.perform(get("/workspace/{wksId}/typos", NOT_EXISTING_WKS_ID))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void getWorkspaceUpdatePageIsSuccessful(final Long wksId, final String email) throws Exception {
        Workspace workspace = repository.getWorkspaceById(wksId).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksId}/update", wksId)
                .with(user(new CustomUserDetails(email, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER"))))))
            .andExpect(model().attributeExists("wksName", "formModified", "formModified"))
            .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains(workspace.getDescription());
    }

    @Test
    void getWorkspaceUpdatePageWithoutWks() throws Exception {
        mockMvc.perform(get("/workspace/{wksId}/update", NOT_EXISTING_WKS_ID))
            .andExpect(redirectedUrl("/workspaces"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void putWorkspaceUpdateIsSuccessful(final Long wksId, final String username) throws Exception {
        Workspace workspace = repository.getWorkspaceById(wksId).orElse(null);
        String newWksName = "createWksName01";
        Instant previousModifiedDate = workspace.getModifiedDate();

        mockMvc.perform(put("/workspace/{wksId}/update", wksId)
                .param("name", newWksName)
                .param("url", "https://other.com")
                .param("description", "Wks description 01")
                .with(user(new CustomUserDetails(username, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                .with(csrf()))
            .andExpect(redirectedUrl("/workspace/" + wksId));

        Instant newModifiedDate = repository.getWorkspaceById(wksId).orElse(null).getModifiedDate();
        assertThat(previousModifiedDate).isNotEqualTo(newModifiedDate);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void putWorkspaceUpdateWithExistingWksUpdateName(final Long wksId, final String username) throws Exception {
        String newWksName = "createWksName01";
        String wksDescription = "Wks description";
        String wksUrl = "https://other.com";

        Instant previousModifiedDate = repository.getWorkspaceById(wksId).orElse(null).getModifiedDate();

        var createWks = new CreateWorkspace(newWksName, wksDescription, wksUrl);
        final var wksToCreate = requireNonNull(workspaceMapper.toWorkspace(createWks));
        final var wksSettings = new WorkspaceSettings();
        wksSettings.setWorkspace(wksToCreate);
        wksSettings.setApiAccessToken(UUID.randomUUID());
        workspaceRepository.save(wksToCreate);

        assertThat(repository.existsWorkspaceByName(newWksName)).isTrue();

        mockMvc.perform(put("/workspace/{wksId}/update", wksId)
                .param("name", newWksName)
                .param("description", wksDescription)
                .with(user(new CustomUserDetails(username, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                .with(csrf()))
            .andExpect(model().attributeExists("createWorkspace"));

        Instant newModifiedDate = repository.getWorkspaceById(wksId).orElse(null).getModifiedDate();
        assertThat(previousModifiedDate).isEqualTo(newModifiedDate);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void putWorkspaceUpdateWithExistingWksUpdateUrl(final Long wksId, final String username) throws Exception {
        String newWksName = "createWksName01";
        String wksDescription = "Wks description";
        String newWksUrl = "https://other.com";

        Workspace wks = repository.getWorkspaceById(wksId).orElse(null);
        Instant previousModifiedDate = wks.getModifiedDate();

        var createWks = new CreateWorkspace(newWksName, wksDescription, newWksUrl);
        final var wksToCreate = requireNonNull(workspaceMapper.toWorkspace(createWks));
        final var wksSettings = new WorkspaceSettings();
        wksSettings.setWorkspace(wksToCreate);
        wksSettings.setApiAccessToken(UUID.randomUUID());
        workspaceRepository.save(wksToCreate);

        assertThat(repository.existsWorkspaceByName(newWksName)).isTrue();

        mockMvc.perform(put("/workspace/{wksId}/update", wksId)
                .param("name", wks.getName())
                .param("description", wksDescription)
                .param("url", newWksUrl)
                .with(user(new CustomUserDetails(username, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                .with(csrf()))
            .andExpect(model().attributeExists("createWorkspace"));

        Instant newModifiedDate = repository.getWorkspaceById(wksId).orElse(null).getModifiedDate();
        assertThat(previousModifiedDate).isEqualTo(newModifiedDate);
    }

    //TODO tests for concurrency transactions in putWorkspaceUpdate() try-catch block

    @Disabled
    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void deleteWorkspaceByIdIsSuccessful(final Long wksId,
                                         final String username) throws Exception {

        assertThat(repository.existsWorkspaceById(wksId)).isTrue();

        MockHttpServletResponse response = mockMvc.perform(
                delete("/workspace/{wksId}", wksId)
                    .with(user(new CustomUserDetails(username, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                    .with(csrf()))
            .andReturn()
            .getResponse();

        assertThat(response.getRedirectedUrl()).isEqualTo("/workspaces");

        // TODO fix: 'repository.existsWorkspaceByName(wksName)' returns true after the wks deleted
        // assertThat(repository.existsWorkspaceByName(wksName)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void getWorkspaceUsersPage(final Long wksId, final String email) throws Exception {
        Workspace workspace = repository.getWorkspaceById(wksId).orElseThrow();
        Set<Account> accounts = new HashSet<>(accountRepository.findAll());

        accounts.forEach(account -> {
            final var workspaceRoleId = new WorkspaceRoleId(workspace.getId(), account.getId());
            final var workspaceRole = new WorkspaceRole(workspaceRoleId, AccountRole.ROLE_ANONYMOUS,
                workspace, account);
            workspace.addWorkspaceRole(workspaceRole);
        });

        MockHttpServletResponse response = mockMvc.perform(
                get("/workspace/{wksId}/users", wksId)
                    .with(user(new CustomUserDetails(email, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER"))))))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("wksInfo", "wksName",
                "userPage", "availableSizes", "sortProp", "sortDir", "DESC", "ASC"))
            .andReturn().getResponse();

        var html = response.getContentAsString();
        for (var wksRole : workspace.getWorkspaceRoles()) {
            var userEmail = wksRole.getAccount().getEmail();
            assertThat(html).contains(userEmail);
        }
    }

    @Test
    void delUserFromWorkspace() throws Exception {
        final Long rolesCountBeforeAdding = workspaceRoleRepository.count();

        mockMvc.perform(
            post("/workspace/{wksId}/users", WORKSPACE_103_ID)
                .param("email", ACCOUNT_102_EMAIL)
                .with(user(new CustomUserDetails(ACCOUNT_103_EMAIL, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                .with(csrf()));
        assertThat(workspaceRoleRepository.count()).isEqualTo(rolesCountBeforeAdding + 1L);
        Optional<WorkspaceRole> addedWksRoleOptional = workspaceRoleRepository
            .getWorkspaceRoleByAccountIdAndWorkspaceId(ACCOUNT_102_ID, WORKSPACE_103_ID);
        assertThat(addedWksRoleOptional).isNotEmpty();

        mockMvc.perform(
            delete("/workspace/{wksId}/users", WORKSPACE_103_ID)
                .param("email", ACCOUNT_102_EMAIL)
                .with(user(new CustomUserDetails(ACCOUNT_102_EMAIL, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                .with(csrf()));
        assertThat(workspaceRoleRepository.count()).isEqualTo(rolesCountBeforeAdding + 1L);
        Optional<WorkspaceRole> addedWksRoleStillThereOptional = workspaceRoleRepository
            .getWorkspaceRoleByAccountIdAndWorkspaceId(ACCOUNT_102_ID, WORKSPACE_103_ID);
        assertThat(addedWksRoleStillThereOptional).isNotEmpty();

        mockMvc.perform(
            delete("/workspace/{wksId}/users", WORKSPACE_103_ID)
                .param("email", ACCOUNT_102_EMAIL)
                .with(user(new CustomUserDetails(ACCOUNT_103_EMAIL, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                .with(csrf()));
        assertThat(workspaceRoleRepository.count()).isEqualTo(rolesCountBeforeAdding);
        Optional<WorkspaceRole> addedWksRoleDeletedOptional = workspaceRoleRepository
            .getWorkspaceRoleByAccountIdAndWorkspaceId(ACCOUNT_102_ID, WORKSPACE_103_ID);
        assertThat(addedWksRoleDeletedOptional).isEmpty();
    }

    @Test
    void addUserNonValidEmailTest() throws Exception {
        var response = mockMvc.perform(
                post("/workspace/{wksId}/users", WORKSPACE_103_ID)
                    .param("email", ACCOUNT_INCORRECT_EMAIL)
                    .with(user(new CustomUserDetails(ACCOUNT_103_EMAIL, "password", "SampleNickname", List.of(new SimpleGrantedAuthority("USER")))))
                    .with(csrf()))
            .andReturn();
        var body = response.getResponse().getContentAsString();
        assertThat(body).contains(String.format("The email &quot;%s&quot; is not valid", ACCOUNT_INCORRECT_EMAIL));
    }
}



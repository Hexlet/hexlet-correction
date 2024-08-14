package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.workspace.AllowedUrl;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.AllowedUrlRepository;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
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

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ACCOUNT_101_EMAIL;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ALLOWED_URL_101_URL;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.ALLOWED_URL_104_URL;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.WORKSPACE_101_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Testcontainers
@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "workspace_settings.yml", "workspaceRoles.yml", "accounts.yml", "allowedUrls.yml"})
public class WorkspaceSettingsControllerIT {

    @Autowired
    private WorkspaceRepository repository;

    @Autowired
    private AllowedUrlRepository urlRepository;

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private WorkspaceSettingsRepository workspaceSettingsRepository;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void getWorkspaceIntegrationPageIsSuccessful(final Long wksId, final String email) throws Exception {
        final var workspaces =  workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId);
        final var apiAccessToken = workspaces
            .map(s -> s.getId() + ":" + s.getApiAccessToken())
            .map(String::getBytes)
            .map(Base64.getEncoder()::encodeToString)
            .orElse(null);

        final var wksName = workspaces
            .map(m -> m.getWorkspace().getName())
            .orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksId}/integration", wksId.toString())
                .with(user(email)))
            .andExpect(model().attributeExists("wksBasicToken", "wksName"))
            .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains(apiAccessToken);
        assertThat(response.getContentAsString()).contains(wksName);

    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspacesAndUsersRelated")
    void getWorkspaceSettingsPageIsSuccessful(final Long wksId, final String username) throws Exception {
        final var apiAccessToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(s -> s.getId() + ":" + s.getApiAccessToken())
            .map(String::getBytes)
            .map(Base64.getEncoder()::encodeToString)
            .orElse(null);

        Workspace workspace = repository.getWorkspaceById(wksId).orElseThrow();
        Set<AllowedUrl> urls = new HashSet<>(urlRepository.findAll());

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksId}/settings", wksId.toString())
                .with(user(username)))
            .andExpect(model().attributeExists("wksBasicToken",
                                        "wksName",
                                        "urlsPage",
                                        "availableSizes",
                                        "sortDir",
                                        "DESC",
                                        "ASC"))
            .andReturn().getResponse();

        var html = response.getContentAsString();
        for (var wksUrl : workspace.getAllowedUrls()) {
            var url = wksUrl.getUrl();
            assertThat(html).contains(url);
        }

        assertThat(response.getContentAsString()).contains(apiAccessToken);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceAndAdminRelated")
    void patchWorkspaceTokenIsSuccessful(final Long wksId, final String email) throws Exception {
        String previousWksToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(WorkspaceSettings::getApiAccessToken)
            .map(UUID::toString)
            .orElse(null);

        MockHttpServletResponse response = mockMvc.perform(patch("/workspace/{wksId}/token/regenerate", wksId)
                .with(user(email))
                .with(csrf()))
            .andReturn().getResponse();

        String newWksToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(WorkspaceSettings::getApiAccessToken)
            .map(UUID::toString)
            .orElse(null);

        assertThat(previousWksToken).isNotEqualTo(newWksToken);
        assertThat(response.getRedirectedUrl()).isEqualTo("/workspace/" + wksId.toString() + "/settings");
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceAndNotAdminRelated")
    void patchWorkspaceTokenWithNoRights(final Long wksId, final String email) throws Exception {
        String previousWksToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(WorkspaceSettings::getApiAccessToken)
            .map(UUID::toString)
            .orElse(null);

        MockHttpServletResponse response =
            mockMvc.perform(patch("/workspace/{wksId}/token/regenerate", wksId.toString())
                    .with(user(email))
                    .with(csrf()))
                .andReturn().getResponse();

        String newWksToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceId(wksId)
            .map(WorkspaceSettings::getApiAccessToken)
            .map(UUID::toString)
            .orElse(null);

        assertThat(previousWksToken).isEqualTo(newWksToken);
        assertThat(response.getRedirectedUrl()).isEqualTo("/workspaces");
    }

    @Test
    void addAllowedUrlToWorkspace() throws Exception {
        final Long urlsCountBeforeAdding = urlRepository.count();

        mockMvc.perform(
            post("/workspace/{wksId}/allowed-urls", WORKSPACE_101_ID)
                .param("url", "https://other.com")
                .with(user(ACCOUNT_101_EMAIL))
                .with(csrf()));
        assertThat(urlRepository.count()).isEqualTo(urlsCountBeforeAdding + 1L);
        var addedAllowedUrlOptional = urlRepository.findAllowedUrlByUrlAndWorkspaceId("https://other.com",
                                                                        WORKSPACE_101_ID);
        assertThat(addedAllowedUrlOptional).isNotEmpty();

        mockMvc.perform(
            post("/workspace/{wksId}/allowed-urls", WORKSPACE_101_ID)
                .param("url", ALLOWED_URL_101_URL)
                .with(user(ACCOUNT_101_EMAIL))
                .with(csrf()));
        assertThat(urlRepository.count()).isEqualTo(urlsCountBeforeAdding + 1L);
        var oldAllowedUrlOptional = urlRepository.findAllowedUrlByUrlAndWorkspaceId(ALLOWED_URL_101_URL,
                                                                        WORKSPACE_101_ID);
        assertThat(oldAllowedUrlOptional).isNotEmpty();
    }

    @Test
    void delAllowedUrlFromWorkspace() throws Exception {
        final Long urlsCountBeforeAdding = urlRepository.count();

        mockMvc.perform(
            delete("/workspace/{wksId}/allowed-urls", WORKSPACE_101_ID)
                .param("url", ALLOWED_URL_104_URL)
                .with(user(ACCOUNT_101_EMAIL))
                .with(csrf()));
        assertThat(urlRepository.count()).isEqualTo(urlsCountBeforeAdding - 1L);
        var deletedAllowedUrlOptional = urlRepository.findAllowedUrlByUrlAndWorkspaceId(ALLOWED_URL_104_URL,
                                                                        WORKSPACE_101_ID);
        assertThat(deletedAllowedUrlOptional).isEmpty();
    }

}

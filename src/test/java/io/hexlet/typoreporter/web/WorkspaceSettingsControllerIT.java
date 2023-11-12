package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.workspacesettings.WorkspaceSettings;
import io.hexlet.typoreporter.repository.WorkspaceSettingsRepository;
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
import java.util.UUID;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@WithMockUser
@AutoConfigureMockMvc
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "workspace_settings.yml"})
public class WorkspaceSettingsControllerIT {

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
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceIntegrationPageIsSuccessful(final String wksName) throws Exception {
        final var apiAccessToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceName(wksName)
            .map(s -> s.getId() + ":" + s.getApiAccessToken())
            .map(String::getBytes)
            .map(Base64.getEncoder()::encodeToString)
            .orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get("/workspace/{wksName}/integration", wksName))
            .andExpect(model().attributeExists("wksBasicToken"))
            .andReturn().getResponse();

        assertThat(response.getContentAsString()).contains(apiAccessToken);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void patchWorkspaceTokenIsSuccessful(final String wksName) throws Exception {
        String previousWksToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceName(wksName)
            .map(WorkspaceSettings::getApiAccessToken)
            .map(UUID::toString)
            .orElse(null);

        MockHttpServletResponse response = mockMvc.perform(patch("/workspace/{wksName}/token/regenerate", wksName)
                .with(csrf()))
            .andReturn().getResponse();

        String newWksToken = workspaceSettingsRepository.getWorkspaceSettingsByWorkspaceName(wksName)
            .map(WorkspaceSettings::getApiAccessToken)
            .map(UUID::toString)
            .orElse(null);

        assertThat(previousWksToken).isNotEqualTo(newWksToken);
        assertThat(response.getRedirectedUrl()).isEqualTo("/workspace/" + wksName + "/settings");
    }

    @Test
    void patchWorkspaceTokenWithoutWks() throws Exception {
        mockMvc.perform(patch("/workspace/{wksName}/token/regenerate", "notExistsWksName")
                .with(csrf()))
            .andExpect(redirectedUrl("/workspaces"));
    }
}

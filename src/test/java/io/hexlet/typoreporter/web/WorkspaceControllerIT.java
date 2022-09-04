package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.AbstractAuditingEntity;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.repository.WorkspaceRepository;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Set;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.web.Routers.SETTINGS;
import static io.hexlet.typoreporter.web.Routers.UPDATE;
import static io.hexlet.typoreporter.web.Routers.USERS;
import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.WKS_NAME_PATH;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
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

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private WorkspaceRepository repository;

    @Autowired
    private WorkspaceService service;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceInfoPageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH, wksName))
            .andExpect(model().attributeExists("wksInfo", "wksName"))
            .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains(wksName, workspace.getCreatedBy());
    }

    @Test
    void getWorkspaceInfoPageWithoutWks() throws Exception {
        mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH, "notExistsWksName"))
            .andExpect(redirectedUrl("/"));
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceSettingsPageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + SETTINGS, wksName))
            .andExpect(model().attributeExists("wksToken"))
            .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains(workspace.getApiAccessToken().toString());
    }

    @Test
    void getWorkspaceSettingsPageWithoutWks() throws Exception {
        mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + SETTINGS, "notExistsWksName"))
            .andExpect(redirectedUrl("/"));
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceTyposPageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + TYPOS, wksName))
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
        mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + TYPOS, "notExistsWksName"))
            .andExpect(redirectedUrl("/"));
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceUsersPage(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);
        Set<Account> accounts = accountRepository.findAll().stream().collect(Collectors.toSet());
        accounts.forEach(account -> workspace.addAccount(account));

        MockHttpServletResponse response = mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + USERS, wksName))
            .andExpect(model().attributeExists("wksInfo", "wksName", "userPage", "availableSizes", "sortProp", "sortDir", "DESC", "ASC"))
            .andReturn().getResponse();

        for (Account account : workspace.getAccounts()) {
            assertThat(response.getContentAsString()).contains(
                account.getId().toString(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail()
            );
        }
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void getWorkspaceUpdatePageIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);

        MockHttpServletResponse response = mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + UPDATE, wksName))
            .andExpect(model().attributeExists("wksName", "formModified", "formModified"))
            .andReturn().getResponse();
        assertThat(response.getContentAsString()).contains(workspace.getDescription());
    }

    @Test
    void getWorkspaceUpdatePageWithoutWks() throws Exception {
        mockMvc.perform(get(WORKSPACE + WKS_NAME_PATH + UPDATE, "notExistsWksName"))
            .andExpect(redirectedUrl("/"));
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void putWorkspaceUpdateIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);
        String newWksName = "createWksName01";
        LocalDateTime previosModifiedDate = workspace.getModifiedDate();

        mockMvc.perform(put(WORKSPACE + WKS_NAME_PATH + UPDATE, wksName)
                .param("name", newWksName)
                .param("description", "Wks description 01")
                .with(csrf()))
            .andExpect(redirectedUrl(WORKSPACE + "/" + newWksName));

        LocalDateTime newModifiedDate = repository.getWorkspaceByName(newWksName).orElse(null).getModifiedDate();
        assertThat(previosModifiedDate).isNotEqualTo(newModifiedDate);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void putWorkspaceUpdateWithExistingWksUpdateName(final String wksName) throws Exception {
        String newWksName = "createWksName01";
        String wksDescription = "Wks description";

        LocalDateTime previosModifiedDate = repository.getWorkspaceByName(wksName).orElse(null).getModifiedDate();

        service.createWorkspace(new CreateWorkspace(newWksName, wksDescription));
        assertThat(repository.existsWorkspaceByName(newWksName)).isTrue();

        mockMvc.perform(put(WORKSPACE + WKS_NAME_PATH + UPDATE, wksName)
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
    void patchWorkspaceTokenIsSuccessful(final String wksName) throws Exception {
        Workspace workspace = repository.getWorkspaceByName(wksName).orElse(null);
        String previousWksToken = workspace.getApiAccessToken().toString();

        MockHttpServletResponse response = mockMvc.perform(patch(WORKSPACE + WKS_NAME_PATH + "/token/regenerate", wksName)
                .with(csrf()))
            .andReturn().getResponse();

        assertThat(previousWksToken).isEqualTo(String.valueOf(workspace.getApiAccessToken()));
        assertThat(response.getRedirectedUrl()).isEqualTo(WORKSPACE + "/" + wksName + SETTINGS);
    }

    @Test
    void patchWorkspaceTokenWithoutWks() throws Exception {
        mockMvc.perform(patch(WORKSPACE + WKS_NAME_PATH + "/token/regenerate", "notExistsWksName")
                .with(csrf()))
            .andExpect(redirectedUrl("/"));
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void deleteWorkspaceByNameIsSuccessful(final String wksName) throws Exception {
        assertThat(repository.existsWorkspaceByName(wksName)).isTrue();

        MockHttpServletResponse response = mockMvc.perform(delete(WORKSPACE + WKS_NAME_PATH, wksName)
                .with(csrf()))
            .andReturn().getResponse();

        assertThat(response.getRedirectedUrl()).isEqualTo("/");
        assertThat(repository.existsWorkspaceByName(wksName)).isFalse();
    }

    @Test
    void deleteWorkspaceByNameIsNotSuccessful() throws Exception {
        mockMvc.perform(delete(WORKSPACE + WKS_NAME_PATH, "notExistsWksName").with(csrf()))
            .andExpect(redirectedUrl("/"));
    }
}

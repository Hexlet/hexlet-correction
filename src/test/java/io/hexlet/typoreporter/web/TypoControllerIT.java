package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.domain.typo.TypoStatus;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.domain.typo.TypoEvent.CANCEL;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.web.Routers.ID_PATH;
import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Typo.TYPO_STATUS;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspaces.yml", "typos.yml"})
public class TypoControllerIT {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private TypoRepository typoRepository;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoIdsExist")
    void updateTypoStatusIsSuccessful(final Long typoId) throws Exception {
        Typo typo = typoRepository.findById(typoId).orElse(null);
        String wksName = typo.getWorkspace().getName();

        typo.setTypoStatus(TypoStatus.IN_PROGRESS);
        TypoStatus previousStatus = typo.getTypoStatus(); // IN_PROGRESS

        mockMvc.perform(patch(TYPOS + ID_PATH + TYPO_STATUS, typoId)
                .param("wksName", wksName)
                .param("event", CANCEL.name())
                .with(csrf()))
            .andExpect(redirectedUrl(WORKSPACE + "/" + wksName + TYPOS));
        assertThat(previousStatus).isNotEqualTo(typo.getTypoStatus());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoIdsExist")
    void updateTypoStatusWithEventIsEmpty(final Long typoId) throws Exception {
        Typo typo = typoRepository.findById(typoId).orElse(null);
        String wksName = typo.getWorkspace().getName();

        typo.setTypoStatus(TypoStatus.IN_PROGRESS);
        TypoStatus previousStatus = typo.getTypoStatus(); // IN_PROGRESS

        mockMvc.perform(patch(TYPOS + ID_PATH + TYPO_STATUS, typoId)
                .param("wksName", wksName)
                .param("event", "")
                .with(csrf()))
            .andExpect(redirectedUrl(WORKSPACE + "/" + wksName + TYPOS));
        assertThat(previousStatus).isEqualTo(typo.getTypoStatus());
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getWorkspaceNamesExist")
    void updateTypoStatusWithUpdatedTypoIsEmpty(final String wksName) throws Exception {
        final Long NOT_EXIST_TYPO_ID = 11L;
        mockMvc.perform(patch(TYPOS + ID_PATH + TYPO_STATUS, NOT_EXIST_TYPO_ID)
                .param("wksName", wksName)
                .param("event", CANCEL.name())
                .with(csrf()))
            .andExpect(redirectedUrl(WORKSPACE + "/" + wksName + TYPOS));
    }


    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoIdsExist")
    void deleteTypoByIdIsSuccessful(final Long typoId) throws Exception {
        Typo typo = typoRepository.findById(typoId).orElse(null);
        String wksName = typo.getWorkspace().getName();

        assertThat(typoRepository.existsById(typoId)).isTrue();

        mockMvc.perform(delete(TYPOS + ID_PATH, typoId)
                .param("wksName", wksName)
                .with(csrf()))
            .andExpect(redirectedUrl(WORKSPACE + "/" + wksName + TYPOS));

        assertThat(typoRepository.existsById(typoId)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoIdsExist")
    void deleteTypoByIdWithEmptyAndNotExistWksName(final Long typoId) throws Exception {
        assertThat(typoRepository.existsById(typoId)).isTrue();

        String emptyWksName = "";
        mockMvc.perform(delete(TYPOS + ID_PATH, typoId)
                .param("wksName", emptyWksName)
                .with(csrf()))
            .andExpect(redirectedUrl("/workspaces"));

        String notExistsWksName = "noExistsWks";
        mockMvc.perform(delete(TYPOS + ID_PATH, typoId)
                .param("wksName", notExistsWksName)
                .with(csrf()))
            .andExpect(redirectedUrl("/workspaces"));

        assertThat(typoRepository.existsById(typoId)).isTrue();
    }
}

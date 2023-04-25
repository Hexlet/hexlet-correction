package io.hexlet.typoreporter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import io.hexlet.typoreporter.test.asserts.ReportedTypoAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Base64;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.WORKSPACE_101_ID;
import static io.hexlet.typoreporter.test.factory.EntitiesFactory.WORKSPACE_101_TOKEN;
import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
@DataSet(value = {"workspace_settings.yml", "workspaces.yml", "typos.yml"})
class WorkspaceApiIT {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    private SpringDataWebProperties dataWebProperties;

    @Autowired
    private TypoRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @ParameterizedTest
    @MethodSource("io.hexlet.typoreporter.test.factory.EntitiesFactory#getTypoReport")
    void addTypoReport(final TypoReport typoReport) throws Exception {
        final var idTokenBytes = (WORKSPACE_101_ID + ":" + WORKSPACE_101_TOKEN).getBytes();
        final var basicEncodedStr = Base64.getEncoder().encodeToString(idTokenBytes);
        final var content = mockMvc
            .perform(post("/api/workspaces/" + WORKSPACE_101_ID + "/typos")
                .content(objectMapper.writeValueAsString(typoReport))
                .header("Authorization", "Basic " + basicEncodedStr)
                .contentType(APPLICATION_JSON)
            )
//            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(header().exists("Location"))
            .andReturn().getResponse().getContentAsString();
        final var reportedTypo = objectMapper.readValue(content, ReportedTypo.class);

        ReportedTypoAssert.assertThat(reportedTypo).isEqualsToTypoReport(typoReport);
        Assertions.assertThat(reportedTypo.createdDate()).isBeforeOrEqualTo(now());
        Assertions.assertThat(repository.existsById(reportedTypo.id())).isTrue();
    }

    @Test
    void addTypoReportWithPageUrlBlank() throws Exception {
        final var typoJson = """
            {
              "pageUrl": "",
              "reporterName": "reporterName",
              "textTypo": "textTypo"
            }
            """;

        final var idTokenBytes = (WORKSPACE_101_ID + ":" + WORKSPACE_101_TOKEN).getBytes();
        final var basicEncodedStr = Base64.getEncoder().encodeToString(idTokenBytes);
        mockMvc.perform(post("/api/workspaces/" + WORKSPACE_101_ID + "/typos")
                .content(typoJson)
                .header("Authorization", "Basic " + basicEncodedStr)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    void addTypoReportWithPageUrlNull() throws Exception {
        final var typoJson = """
            {
              "reporterName": "reporterName",
              "textTypo": "textTypo"
            }
            """;

        final var idTokenBytes = (WORKSPACE_101_ID + ":" + WORKSPACE_101_TOKEN).getBytes();
        final var basicEncodedStr = Base64.getEncoder().encodeToString(idTokenBytes);
        mockMvc.perform(post("/api/workspaces/" + WORKSPACE_101_ID + "/typos")
                .content(typoJson)
                .header("Authorization", "Basic " + basicEncodedStr)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    void addTypoReportWithPageUrlNotValid() throws Exception {
        final var typoJson = """
            {
              "pageUrl": "not valid",
              "reporterName": "reporterName",
              "textTypo": "textTypo"
            }
            """;

        final var idTokenBytes = (WORKSPACE_101_ID + ":" + WORKSPACE_101_TOKEN).getBytes();
        final var basicEncodedStr = Base64.getEncoder().encodeToString(idTokenBytes);
        mockMvc.perform(post("/api/workspaces/" + WORKSPACE_101_ID + "/typos")
                .content(typoJson)
                .header("Authorization", "Basic " + basicEncodedStr)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }
}



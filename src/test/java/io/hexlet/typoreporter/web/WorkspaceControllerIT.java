package io.hexlet.typoreporter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.repository.TypoRepository;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.*;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.TypoReporterApplicationIT.POSTGRES_IMAGE;

//@Testcontainers
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//@DBRider
//@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
//@DataSet(value = {"workspaces.yml", "typos.yml"})
class WorkspaceControllerIT {
//
//    @Container
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
//        .withPassword("inmemory")
//        .withUsername("inmemory");
//
//    @Autowired
//    private SpringDataWebProperties dataWebProperties;
//
//    private String pageParameter;
//
//    private String sizeParameter;
//
//    private int defaultPageSize;
//
//    private int firstPageNumber;
//
//    @Autowired
//    private TypoRepository repository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @DynamicPropertySource
//    static void datasourceProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//    }
//
//    @BeforeEach
//    void setUp() {
//        pageParameter = dataWebProperties.getPageable().getPageParameter();
//        sizeParameter = dataWebProperties.getPageable().getSizeParameter();
//        defaultPageSize = dataWebProperties.getPageable().getDefaultPageSize();
//        firstPageNumber = dataWebProperties.getPageable().isOneIndexedParameters() ? 1 : 0;
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoReport")
//    void addTypoReport(final TypoReport typoReport) throws Exception {
//        final var content = mockMvc
//            .perform(post(API_TYPOS)
//                .content(objectMapper.writeValueAsString(typoReport))
//                .contentType(APPLICATION_JSON)
//            )
//            .andDo(print())
//            .andExpect(status().isCreated())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(header().exists("Location"))
//            .andReturn().getResponse().getContentAsString();
//        final var reportedTypo = objectMapper.readValue(content, ReportedTypo.class);
//
//        ReportedTypoAssert.assertThat(reportedTypo).isEqualsToTypoReport(typoReport);
//        Assertions.assertThat(reportedTypo.createdDate()).isBeforeOrEqualTo(now());
//        Assertions.assertThat(repository.existsById(reportedTypo.id())).isTrue();
//    }
//
//    @Test
//    void addTypoReportWithPageUrlBlank() throws Exception {
//        final var typoJson = """
//            {
//              "pageUrl": "",
//              "reporterName": "reporterName",
//              "textTypo": "textTypo"
//            }
//            """;
//
//        mockMvc.perform(post(API_TYPOS)
//            .content(typoJson)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
//            .andExpect(jsonPath("$.title").value("Constraint Violation"))
//            .andExpect(jsonPath("$.violations").isArray())
//            .andExpect(jsonPath("$.violations", hasSize(1)))
//            .andExpect(jsonPath("$.violations[*].field", hasItem("pageUrl")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("must not be blank")));
//    }
//
//    @Test
//    void addTypoReportWithPageUrlNull() throws Exception {
//        final var typoJson = """
//            {
//              "reporterName": "reporterName",
//              "textTypo": "textTypo"
//            }
//            """;
//
//        mockMvc.perform(post(API_TYPOS)
//            .content(typoJson)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
//            .andExpect(jsonPath("$.title").value("Constraint Violation"))
//            .andExpect(jsonPath("$.violations").isArray())
//            .andExpect(jsonPath("$.violations", hasSize(2)))
//            .andExpect(jsonPath("$.violations[*].field", hasItem("pageUrl")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("must not be null")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("must not be blank")));
//    }
//
//    @Test
//    void addTypoReportWithPageUrlNotValid() throws Exception {
//        final var typoJson = """
//            {
//              "pageUrl": "not valid",
//              "reporterName": "reporterName",
//              "textTypo": "textTypo"
//            }
//            """;
//
//        mockMvc.perform(post(API_TYPOS)
//            .content(typoJson)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
//            .andExpect(jsonPath("$.title").value("Constraint Violation"))
//            .andExpect(jsonPath("$.violations").isArray())
//            .andExpect(jsonPath("$.violations", hasSize(1)))
//            .andExpect(jsonPath("$.violations[*].field", hasItem("pageUrl")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("must be a valid URL")));
//    }
//
//    @Test
//    void getPageTypoDefault() throws Exception {
//        final var totalSize = repository.count();
//        mockMvc.perform(get(API_TYPOS))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.totalPages").value(totalSize / defaultPageSize + 1))
//            .andExpect(jsonPath("$.totalElements").value(totalSize))
//            .andExpect(jsonPath("$.size").value(defaultPageSize))
//            .andExpect(jsonPath("$.number").value(firstPageNumber))
//            .andExpect(jsonPath("$.numberOfElements").value(totalSize % defaultPageSize));
//    }
//
//    @Test
//    void getPageTypo() throws Exception {
//        final var totalSize = repository.count();
//        final var pageSize = 3;
//        final var page = 1;
//        mockMvc.perform(get(API_TYPOS).param(sizeParameter, pageSize + "").param(pageParameter, page + ""))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.totalPages").value(totalSize / pageSize + 1))
//            .andExpect(jsonPath("$.totalElements").value(totalSize))
//            .andExpect(jsonPath("$.size").value(pageSize))
//            .andExpect(jsonPath("$.number").value(page))
//            .andExpect(jsonPath("$.numberOfElements").value(pageSize));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void getTypoById(final Long id) throws Exception {
//        mockMvc.perform(get(API_TYPOS + ID_PATH, id))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsNotExist")
//    void getTypoByIdNoFound(final Long id) throws Exception {
//        Assertions.assertThat(repository.existsById(id)).isFalse();
//        mockMvc.perform(get(API_TYPOS + ID_PATH, id))
//            .andDo(print())
//            .andExpect(status().isNotFound())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoReporterComment(final Long id) throws Exception {
//        final var typo = repository.findById(id).orElseThrow();
//        final var newComment = typo.getReporterComment() + " <- new comment";
//        final var body = format("'{' \"reporterComment\" : \"{0}\" '}'", newComment);
//        mockMvc.perform(patch(API_TYPOS + ID_PATH, id)
//            .content(body)
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id))
//            .andExpect(jsonPath("$.reporterComment").value(newComment));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoWithEmptyBody(final Long id) throws Exception {
//        final var typo = repository.findById(id).orElseThrow();
//        final var body = "{}";
//        mockMvc.perform(patch(API_TYPOS + ID_PATH, typo.getId())
//            .content(body)
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id))
//            .andExpect(jsonPath("$.reporterComment").value(nullValue()));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoReporterCommentNull(final Long id) throws Exception {
//        final var typo = repository.findById(id).orElseThrow();
//        final var body = "{ \"reporterComment\" : null }";
//        mockMvc.perform(patch(API_TYPOS + ID_PATH, id)
//            .content(body)
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id))
//            .andExpect(jsonPath("$.reporterComment").value(nullValue()));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoReporterCommentEmptyNotValid(final Long id) throws Exception {
//        final var body = "{ \"reporterComment\" : \"\" }";
//        mockMvc.perform(patch(API_TYPOS + ID_PATH, id)
//            .content(body)
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isBadRequest())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
//            .andExpect(jsonPath("$.title").value("Constraint Violation"))
//            .andExpect(jsonPath("$.violations").isArray())
//            .andExpect(jsonPath("$.violations", hasSize(2)))
//            .andExpect(jsonPath("$.violations[*].field", hasItem("reporterComment")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("Reporter comment must be null")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("Reporter comment must not be blank")));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoReporterCommentBlankNotValid(final Long id) throws Exception {
//        final var body = "{ \"reporterComment\" : \"   \" }";
//        mockMvc.perform(patch(API_TYPOS + ID_PATH, id)
//            .content(body)
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isBadRequest())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
//            .andExpect(jsonPath("$.title").value("Constraint Violation"))
//            .andExpect(jsonPath("$.violations").isArray())
//            .andExpect(jsonPath("$.violations", hasSize(2)))
//            .andExpect(jsonPath("$.violations[*].field", hasItem("reporterComment")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("Reporter comment must be null")))
//            .andExpect(jsonPath("$.violations[*].message", hasItem("Reporter comment must not be blank")));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoEventResolveToResolved(final Long id) throws Exception {
//        repository.findById(id)
//            .map(t -> t.setTypoStatus(t.getTypoStatus().next(OPEN)))
//            .map(repository::save)
//            .orElseThrow();
//        final var updateTypo = new UpdateTypoEvent(id, RESOLVE);
//        mockMvc.perform(patch(API_TYPOS + Typo.ID_PATH_TYPO_STATUS, id)
//            .content(objectMapper.writeValueAsString(updateTypo))
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id))
//            .andExpect(jsonPath("$.typoStatus", is(RESOLVED + "")));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoEventOpenToInProgress(final Long id) throws Exception {
//        final var patchTypo = new UpdateTypoEvent(id, OPEN);
//        mockMvc.perform(patch(API_TYPOS + Typo.ID_PATH_TYPO_STATUS, id)
//            .content(objectMapper.writeValueAsString(patchTypo))
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id))
//            .andExpect(jsonPath("$.typoStatus", is(IN_PROGRESS + "")));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoEventCancelToCanceled(final Long id) throws Exception {
//        final var patchTypo = new UpdateTypoEvent(id, CANCEL);
//        mockMvc.perform(patch(API_TYPOS + Typo.ID_PATH_TYPO_STATUS, id)
//            .content(objectMapper.writeValueAsString(patchTypo))
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(APPLICATION_JSON))
//            .andExpect(jsonPath("$.id").value(id))
//            .andExpect(jsonPath("$.typoStatus", is(CANCELED + "")));
//    }
//
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void patchTypoEventResolveNotValid(final Long id) throws Exception {
//        final var patchTypo = new UpdateTypoEvent(id, RESOLVE);
//        mockMvc.perform(patch(API_TYPOS + Typo.ID_PATH_TYPO_STATUS, id)
//            .content(objectMapper.writeValueAsString(patchTypo))
//            .contentType(APPLICATION_JSON))
//            .andDo(print())
//            .andExpect(status().isBadRequest())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsExist")
//    void deleteTypoById(final Long id) throws Exception {
//        Assertions.assertThat(repository.existsById(id)).isTrue();
//        mockMvc.perform(delete(API_TYPOS + ID_PATH, id))
//            .andDo(print())
//            .andExpect(status().isOk());
//        Assertions.assertThat(repository.existsById(id)).isFalse();
//    }
//
//    @ParameterizedTest
//    @MethodSource("io.hexlet.typoreporter.test.utils.EntitiesFactory#getTypoIdsNotExist")
//    void deleteTypoByIdNotFound(final Long id) throws Exception {
//        Assertions.assertThat(repository.existsById(id)).isFalse();
//        mockMvc.perform(delete(API_TYPOS + ID_PATH, id))
//            .andDo(print())
//            .andExpect(status().isNotFound())
//            .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
//        Assertions.assertThat(repository.existsById(id)).isFalse();
//    }
}



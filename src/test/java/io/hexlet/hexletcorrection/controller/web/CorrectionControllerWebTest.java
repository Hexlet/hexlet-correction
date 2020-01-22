package io.hexlet.hexletcorrection.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.CORRECTIONS_PATH;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrectionControllerWebTest extends AbstractControllerTest {

    private static long EXISTED_CORRECTION_ID = 1L;

    private static long NON_EXISTED_CORRECTION_ID = 1000L;

    private final String CONTENT_TYPE = ContentType.HTML.withCharset(StandardCharsets.UTF_8);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    @Before
    public void init() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        when(correctionService.findById(EXISTED_CORRECTION_ID)).thenReturn(
            Optional.of(createCorrectionWithId(EXISTED_CORRECTION_ID, createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com")))
        );
    }

    @WithMockUser
    @Test
    public void getCorrectionById() throws Exception {
        mvc.perform(get(CORRECTIONS_PATH + "/" + EXISTED_CORRECTION_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", EXISTED_CORRECTION_ID))))
            .andExpect(content().string(containsString(format("<td>%s</td>", "hexlet.io"))))
            .andExpect(content().string(containsString(format("<td>%s</td>", "text to correction"))));
    }

    @WithMockUser
    @Test
    public void getCorrectionsByIdNotFound() throws Exception {
        when(correctionService.findById(NON_EXISTED_CORRECTION_ID)).thenReturn(Optional.empty());

        mvc.perform(get(CORRECTIONS_PATH + "/" + NON_EXISTED_CORRECTION_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("Correction with id=%d not found", NON_EXISTED_CORRECTION_ID))));
    }

    @WithMockUser
    @Test
    public void getCreationForm() throws Exception {
        mvc.perform(get(CORRECTIONS_PATH + "/new"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE));
    }
}

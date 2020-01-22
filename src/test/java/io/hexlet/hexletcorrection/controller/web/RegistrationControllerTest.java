package io.hexlet.hexletcorrection.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.REGISTRATION_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.restassured.RestAssured.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    public void registrationPageTest() {
        given().when()
            .get(TEST_HOST + ":" + port + REGISTRATION_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.HTML);
    }

    @WithMockUser
    @Test
    public void postAccount() throws Exception {
        mvc.perform(
            post(REGISTRATION_PATH)
                .param("email", "testAccounts@mail.com")
                .param("name", DEFAULT_USER_NAME)
                .param("password", DEFAULT_USER_PASSWORD)
                .param("passwordConfirm", DEFAULT_USER_PASSWORD)
                .with(csrf())
        ).andExpect(status().isFound());
    }
}

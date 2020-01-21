package io.hexlet.hexletcorrection.validate.jsonschema;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountValidateSchema extends AbstractControllerTest {

    private static long EXISTED_USER_ID = 1L;

    @LocalServerPort
    private int port;

    @Test
    public void SchemaValidationTest() {
        InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("jsonschema/account.json");
        when(accountService.findById(EXISTED_USER_ID)).thenReturn(Optional.of(createAccountWithId(EXISTED_USER_ID, DEFAULT_USER_NAME, "testCorrections@mail.com")));

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + EXISTED_USER_ID)
                .then()
                .assertThat()
                .body(matchesJsonSchema(schema));
    }
}

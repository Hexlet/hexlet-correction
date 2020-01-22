package io.hexlet.hexletcorrection.validate.jsonschema;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.CORRECTIONS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrectionValidateSchema extends AbstractControllerTest {

    private long EXISTED_CORRECTION_ID = 1L;

    @LocalServerPort
    private int port;

    @Test
    public void SchemaValidationTest() {
        InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("jsonschema/correction.json");
        when(correctionService.findById(EXISTED_CORRECTION_ID)).thenReturn(
            Optional.of(createCorrectionWithId(EXISTED_CORRECTION_ID, createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com")))
        );

        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + EXISTED_CORRECTION_ID)
            .then()
            .assertThat()
            .body(matchesJsonSchema(schema));
    }
}

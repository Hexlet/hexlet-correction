package io.hexlet.hexletcorrection.validate.jsonschema;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountValidateSchema extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void SchemaValidationTest() {
        val name = "bob";
        val email = "bob@gmail.com";
        val account = createAccount(name, email);
        InputStream schema = Thread.currentThread().getContextClassLoader().getResourceAsStream("jsonschema/account.json");

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + account.getId())
                .then()
                .assertThat()
                .body(matchesJsonSchema(schema));
    }
}

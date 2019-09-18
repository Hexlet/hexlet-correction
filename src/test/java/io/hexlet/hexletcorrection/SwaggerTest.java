package io.hexlet.hexletcorrection;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.SWAGGER_API_DOCS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SWAGGER_UI_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerTest {

    @LocalServerPort
    private int port;

    @Test
    public void swaggerUiLoadingTest() {
        given().when()
                .get(TEST_HOST + ":" + port + SWAGGER_UI_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    public void getSwaggerJsonTest() {
        given().when()
                .get(TEST_HOST + ":" + port + SWAGGER_API_DOCS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType("text/html;charset=UTF-8");
    }
}

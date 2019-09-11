package io.hexlet.hexletcorrection;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SwaggerTest {

    @LocalServerPort
    private int port;

    private static final String TEST_HOST = "http://localhost";
    private static final String SWAGGER_UI_PATH = "/swagger-ui.html";
    private static final String SWAGGER_API_DOCS_PATH = "/v2/api-docs?group=full-api";

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
                .contentType(ContentType.JSON);
    }
}

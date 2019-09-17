package io.hexlet.hexletcorrection.controller;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.LOGIN_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void loginPageTest() {
        given().when()
                .get(TEST_HOST + ":" + port + LOGIN_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }
}

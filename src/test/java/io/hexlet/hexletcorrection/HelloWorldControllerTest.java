package io.hexlet.hexletcorrection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloWorldControllerTest {

    private final static String HOST = "http://localhost";

    @LocalServerPort
    private int port;

    @Test
    public void helloWorldTest() {
        given().when().get(HOST + ":" + port + "/").then().statusCode(HttpStatus.OK.value());
    }
}

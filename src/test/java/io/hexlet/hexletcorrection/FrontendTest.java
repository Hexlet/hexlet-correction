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
public class FrontendTest {

    @LocalServerPort
    private int port;

    private static final String ROOT_PATH = "/";
    private static final String REACT_APP_BUNDLE_PATH = "/dist/app-bundle.js";
    private static final String TEST_HOST = "http://localhost";

    @Test
    public void getRootPathTest() {
        given().when()
                .get(TEST_HOST + ":" + port + ROOT_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.HTML);
    }

    @Test
    public void getReactAppBundleTest() {
        given().when()
                .get(TEST_HOST + ":" + port + REACT_APP_BUNDLE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.fromContentType("application/javascript"));
    }

}

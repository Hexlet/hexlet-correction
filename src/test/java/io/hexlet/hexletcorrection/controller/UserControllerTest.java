package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    UserService userService;

    private final static String HOST = "http://localhost";

    private final static String USER_PATH = "/users";

    @LocalServerPort
    private int port;

    @Test
    public void getAllUsersTest() {
        given().when().get(HOST + ":" + port + USER_PATH).then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getUserByIdTest() {
        User user = createUser();

        given().when().get(HOST + ":" + port + USER_PATH + "/" + user.getId())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void findUserByNameTest() {
        User user = createUser();

        given().when().get(HOST + ":" + port + USER_PATH + "/?name=" + user.getName())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void postUserTest() {
        User user = User.builder().name("Artem").email("artem@hexlet.io").build();

        given().when().body(user).contentType(ContentType.JSON)
                .post(HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deleteUserTest() {
        User user = createUser();

        given().when().delete(HOST + ":" + port + USER_PATH + "/" + user.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private User createUser() {
        User user = User.builder().name("Artem").email("artem@hexlet.io").build();

        return userService.create(user);
    }
}

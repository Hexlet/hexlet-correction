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

import static io.hexlet.hexletcorrection.controller.ControllerConstrainConstants.TEST_HOST;
import static io.hexlet.hexletcorrection.controller.ControllerConstrainConstants.USER_PATH;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.INVALID_EMAIL;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_LENGTH_USER_NAME;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    UserService userService;

    @LocalServerPort
    private int port;

    @Test
    public void getAllUsersTest() {
        given().when().get(TEST_HOST + ":" + port + USER_PATH).then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getUserByIdTest() {
        User user = createUser();

        given().when().get(TEST_HOST + ":" + port + USER_PATH + "/" + user.getId())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getUserByFalseIdTest() {
        User user = createUser();

        given().when().get(TEST_HOST + ":" + port + USER_PATH + "/" + 999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("user", equalTo("User with ID '999' not found"));
    }

    @Test
    public void getUserByNameTest() {
        User user = createUser();

        given().when().get(TEST_HOST + ":" + port + USER_PATH + "/?name=" + user.getName())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getUserByFalseNameTest() {
        createUser();

        String falseName = "falseName";

        given().when().get(TEST_HOST + ":" + port + USER_PATH + "/?name=" + falseName)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void postUserTest() {
        User user = User.builder().name("Artem").email("artem@hexlet.io").build();

        given().when().body(user).contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postUserNameEmptyTest() {
        User user = User.builder().email("artem@hexlet.io").build();
        given().when().body(user).contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name " + NOT_EMPTY));
    }

    @Test
    public void postUserNameTooLongTest() {
        User user = User.builder()
                .email("artem@hexlet.io")
                .name("A".repeat(MAX_LENGTH_USER_NAME + 1)).build();

        given().when().body(user).contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name not be more than " + MAX_LENGTH_USER_NAME + " characters"));
    }

    @Test
    public void postUserEmailEmptyTest() {
        User user = User.builder().name("Artem").build();
        given().when().body(user).contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("email", equalTo("Email " + NOT_EMPTY));
    }

    @Test
    public void postUserEmailInvalidTest() {
        User user = User.builder().name("Artem").email("123").build();
        given().when().body(user).contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    public void deleteUserTest() {
        User user = createUser();

        given().when().delete(TEST_HOST + ":" + port + USER_PATH + "/" + user.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private User createUser() {
        User user = User.builder().name("Artem").email("artem@hexlet.io").build();

        return userService.create(user);
    }
}

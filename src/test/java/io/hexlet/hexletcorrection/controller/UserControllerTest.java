package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.service.UserService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    UserService userService;

    private final static String HOST = "http://localhost";

    private final static String USER_PATH = "/users";

    @LocalServerPort
    private int port;

    @Value("${hexlet.user.maxUserName}")
    private int maxUserName;

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
    public void getUserByFalseIdTest() {
        User user = createUser();

        given().when().get(HOST + ":" + port + USER_PATH + "/" + 999)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("user", equalTo("User with ID '999' not found"));
    }

    @Test
    public void getUserByNameTest() {
        User user = createUser();

        given().when().get(HOST + ":" + port + USER_PATH + "/?name=" + user.getName())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getUserByFalseNameTest() {
        User user = createUser();

        String falseName = "falseName";

        given().when().get(HOST + ":" + port + USER_PATH + "/?name=" + falseName)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("user", equalTo("User with name 'falseName' not found"));
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
    public void postUserNameEmptyTest() {
        User user = User.builder().email("artem@hexlet.io").build();
        given().when().body(user).contentType(ContentType.JSON)
                .post(HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name not be empty"));
    }

    @Test
    public void postUserNameTooLongTest() {
        User user = User.builder()
                .email("artem@hexlet.io")
                .name("A".repeat(++maxUserName)).build();

        given().when().body(user).contentType(ContentType.JSON)
                .post(HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name not be more than 50 characters"));
    }

    @Test
    public void postUserEmailEmptyTest() {
        User user = User.builder().name("Artem").build();
        given().when().body(user).contentType(ContentType.JSON)
                .post(HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("email", equalTo("Email not be empty"));
    }

    @Test
    public void postUserEmailInvalidTest() {
        User user = User.builder().name("Artem").email("123").build();
        given().when().body(user).contentType(ContentType.JSON)
                .post(HOST + ":" + port + USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("email", equalTo("Invalid Email provided"));
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

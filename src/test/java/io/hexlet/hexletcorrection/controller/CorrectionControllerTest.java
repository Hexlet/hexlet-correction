package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.service.CorrectionService;
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
public class CorrectionControllerTest {

    @Autowired
    CorrectionService correctionService;

    @Autowired
    UserService userService;

    private final static String HOST = "http://localhost";

    private final static String CORRECTIONS_PATH = "/corrections";

    @LocalServerPort
    private int port;

    @Value("${hexlet.correction.maxComment}")
    private int maxComment;

    @Test
    public void getAllCorrectionsTest() {
        given().when().get(HOST + ":" + port + CORRECTIONS_PATH).then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getCorrectionByIdTest() {
        Correction savedCorrection = createCorrection(createUser());

        given().when().get(HOST + ":" + port + CORRECTIONS_PATH + "/" + savedCorrection.getId())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getCorrectionByUrlTest() {
        Correction savedCorrection = createCorrection(createUser());

        given().when().get(HOST + ":" + port + CORRECTIONS_PATH + "/?url=" + savedCorrection.getPageURL())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void postCorrectionTest() {
        User user = createUser();

        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .user(user)
                .build();

        given().when().body(correction).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postCorrectionCommentEmptyTest() {
        Correction correction = Correction.builder()
                .comment("")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .user(createUser())
                .build();

        given().when().body(correction).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("comment", equalTo("Comment not be empty"));
    }

    @Test
    public void postCorrectionCommentTooLongTest() {
        Correction correction = Correction.builder()
                .comment("A".repeat(++maxComment))
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .user(createUser())
                .build();

        given().when().body(correction).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("comment", equalTo("Comment not be more than 200 characters"));
    }

    @Test
    public void postCorrectionHighlightTextEmptyTest() {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("")
                .pageURL("hexlet.io")
                .user(createUser())
                .build();

        given().when().body(correction).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("highlightText", equalTo("Highlight text not be empty"));
    }

    @Test
    public void postCorrectionUserNullTest() {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .build();

        given().when().body(correction).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("user", equalTo("User not be null"));
    }

    @Test
    public void postCorrectionURLEmptyTest() {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .user(createUser())
                .build();

        given().when().body(correction).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("pageURL", equalTo("URL not be empty"));
    }

    @Test
    public void deleteCorrectionTest() {
        Correction savedCorrection = createCorrection(createUser());

        given().when().delete(HOST + ":" + port + CORRECTIONS_PATH + "/" + savedCorrection.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private User createUser() {
        User user = User.builder().name("Artem").email("artem@hexlet.io").build();

        return userService.create(user);
    }

    private Correction createCorrection(User user) {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .user(createUser())
                .build();

        return correctionService.create(correction);
    }
}

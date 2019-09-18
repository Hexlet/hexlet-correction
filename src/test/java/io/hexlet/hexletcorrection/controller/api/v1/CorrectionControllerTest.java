package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.CORRECTIONS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_COMMENT_LENGTH;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_NULL;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrectionControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void getAllCorrectionsTest() {
        createCorrection(createAccount(DEFAULT_USER_NAME, "getAllCorrections@mail.com"));
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAllCorrectionsRecursionInfiniteTest() {
        createCorrection(createAccount(DEFAULT_USER_NAME, "getAllCorrectionsRecursionInfinite@mail.com"));
        List<Correction> list = given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getList("account.corrections", Correction.class);
        Assert.assertNull(list.get(0));
        assertThatExceptionOfType(StackOverflowError.class);
    }

    @Test
    public void getCorrectionByIdTest() {
        Correction savedCorrection = createCorrection(createAccount(DEFAULT_USER_NAME, "getCorrectionById@mail.com"));

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + savedCorrection.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getCorrectionByFalseIdTest() {
        long falseId = createCorrection(createAccount(DEFAULT_USER_NAME, "getCorrectionByFalseId@mail.com")).getId() + 1L;

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + falseId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("correction", equalTo("Correction with ID '" + falseId + "' not found"));
    }

    @Test
    public void getCorrectionByUrlTest() {
        Correction savedCorrection = createCorrection(createAccount(DEFAULT_USER_NAME, "getCorrectionByUrl@mail.com"));

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/?url=" + savedCorrection.getPageURL())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void postCorrectionTest() {
        Account account = createAccount(DEFAULT_USER_NAME, "postCorrection@mail.com");

        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .account(account)
                .build();

        given().when()
                .body(correction)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postCorrectionCommentEmptyTest() {
        Correction correction = Correction.builder()
                .comment("")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .account(createAccount(DEFAULT_USER_NAME,"postCorrectionCommentEmpty@mail.com"))
                .build();

        given().when()
                .body(correction)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("comment", equalTo("Comment " + NOT_EMPTY));
    }

    @Test
    public void postCorrectionCommentTooLongTest() {
        Correction correction = Correction.builder()
                .comment("A".repeat(MAX_COMMENT_LENGTH + 1))
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .account(createAccount(DEFAULT_USER_NAME, "postCorrectionCommentTooLong@mail.com"))
                .build();

        given().when()
                .body(correction)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("comment", equalTo("Comment not be more than " + MAX_COMMENT_LENGTH + " characters"));
    }

    @Test
    public void postCorrectionHighlightTextEmptyTest() {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("")
                .pageURL("hexlet.io")
                .account(createAccount(DEFAULT_USER_NAME, "postCorrectionHighlightTextEmpty@mail.com"))
                .build();

        given().when()
                .body(correction)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("highlightText", equalTo("Highlight text " + NOT_EMPTY));
    }

    @Test
    public void postCorrectionAccountNullTest() {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .build();

        given().when()
                .body(correction)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("account", equalTo("Account " + NOT_NULL));
    }

    @Test
    public void postCorrectionURLEmptyTest() {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .account(createAccount(DEFAULT_USER_NAME, "postCorrectionURLEmpty@mail.com"))
                .build();

        given().when()
                .body(correction)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("pageURL", equalTo("URL " + NOT_EMPTY));
    }

    @Test
    public void deleteCorrectionTest() {
        Correction savedCorrection = createCorrection(createAccount(DEFAULT_USER_NAME, "deleteCorrection@mail.com"));

        given().when()
                .delete(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + savedCorrection.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}

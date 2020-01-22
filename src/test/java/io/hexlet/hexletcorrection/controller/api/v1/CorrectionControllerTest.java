package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.hexlet.hexletcorrection.domain.Correction;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.CORRECTIONS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_COMMENT_LENGTH;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrectionControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    private static long EXISTED_CORRECTION_ID = 1L;

    private static long NON_EXISTED_CORRECTION_ID = 1000L;

    @Before
    public void init() {
        when(correctionService.findById(EXISTED_CORRECTION_ID)).thenReturn(
            Optional.of(createCorrectionWithId(EXISTED_CORRECTION_ID, createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com")))
        );
    }

    @Test
    public void getAllCorrectionsTest() {
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON);
    }

    @Test
    public void getCorrectionByIdTest() {
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + EXISTED_CORRECTION_ID)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON);
    }

    @Test
    public void getCorrectionByFalseIdTest() {
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + NON_EXISTED_CORRECTION_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .contentType(ContentType.JSON)
            .body("correction", equalTo("Correction with ID '" + NON_EXISTED_CORRECTION_ID + "' not found"));
    }

    @Test
    public void getCorrectionByUrlTest() {
        when(correctionService.findByURL(any())).thenReturn(
            Arrays.asList(createCorrectionWithId(EXISTED_CORRECTION_ID, createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com")))
        );
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/?url=http:\\\\test.com")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON);
    }

    @Test
    public void postCorrectionTest() {
        Correction correction = createCorrection(
            createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com")
        );

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
            .account(createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com"))
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
            .account(createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com"))
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
            .account(createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com"))
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
    public void postCorrectionURLEmptyTest() {
        Correction correction = Correction.builder()
            .comment("test comment")
            .highlightText("text to correction")
            .account(createAccountWithId(EXISTED_CORRECTION_ID, DEFAULT_USER_NAME, "testCorrections@mail.com"))
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
        given().when()
            .delete(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + EXISTED_CORRECTION_ID)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}

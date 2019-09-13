package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.service.CorrectionService;
import io.hexlet.hexletcorrection.service.AccountService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.*;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_COMMENT_LENGTH;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_NULL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrectionControllerTest {

    @Autowired
    CorrectionService correctionService;

    @Autowired
    AccountService accountService;

    @LocalServerPort
    private int port;

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
        Correction savedCorrection = createCorrection(createAccount());

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + savedCorrection.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getCorrectionByFalseIdTest() {
        long falseId = createCorrection(createAccount()).getId() + 1L;

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + falseId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("correction", equalTo("Correction with ID '" + falseId + "' not found"));
    }

    @Test
    public void getCorrectionByUrlTest() {
        Correction savedCorrection = createCorrection(createAccount());

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/?url=" + savedCorrection.getPageURL())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void postCorrectionTest() {
        Account account = createAccount();

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
                .account(createAccount())
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
                .account(createAccount())
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
                .account(createAccount())
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
                .account(createAccount())
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
        Correction savedCorrection = createCorrection(createAccount());

        given().when()
                .delete(TEST_HOST + ":" + port + API_PATH_V1 + CORRECTIONS_PATH + "/" + savedCorrection.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private Account createAccount() {
        Account account = Account.builder()
                .name("Artem")
                .email("artem@hexlet.io")
                .build();

        return accountService.create(account);
    }

    private Correction createCorrection(Account account) {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .account(account)
                .build();

        return correctionService.create(correction);
    }
}

package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.service.AccountService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.INVALID_EMAIL;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_ACCOUNT_NAME;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    AccountService accountService;

    @LocalServerPort
    private int port;

    @Test
    public void getAllAccountsTest() {
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAccountByIdTest() {
        Account account = createAccount();

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + account.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAccountByFalseIdTest() {
        long falseId = createAccount().getId() + 1L;

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + falseId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("account", equalTo("Account with ID '" + falseId + "' not found"));
    }

    @Test
    public void getAccountByNameTest() {
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/?name=" + createAccount().getName())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAccountByFalseNameTest() {
        String falseName = createAccount().getName() + "A";

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/?name=" + falseName)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void postAccountTest() {
        Account account = Account.builder()
                .name("Artem")
                .email("artem@hexlet.io")
                .build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postAccountTestNotSameIdDuringUpdate() {
        Account account = Account.builder()
                .name("Artem")
                .email("artem@hexlet.io")
                .build();

        long firstId = given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body().jsonPath().getLong("id");

        account.setId(firstId);

        long secondId = given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body().jsonPath().getLong("id");

        assertNotEquals(firstId, secondId);
    }

    @Test
    public void postAccountNameEmptyTest() {
        Account account = Account.builder()
                .email("artem@hexlet.io")
                .build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name " + NOT_EMPTY));
    }

    @Test
    public void postAccountNameTooLongTest() {
        Account account = Account.builder()
                .email("artem@hexlet.io")
                .name("A".repeat(MAX_ACCOUNT_NAME + 1)).build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("name", equalTo("Name not be more than " + MAX_ACCOUNT_NAME + " characters"));
    }

    @Test
    public void postAccountEmailEmptyTest() {
        Account account = Account.builder()
                .name("Artem")
                .build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("email", equalTo("Email " + NOT_EMPTY));
    }

    @Test
    public void postAccountEmailInvalidTest() {
        Account account = Account.builder()
                .name("Artem")
                .email("123")
                .build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("email", equalTo(INVALID_EMAIL));
    }

    @Test
    public void deleteAccountTest() {
        Account account = createAccount();

        given().when()
                .delete(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + account.getId())
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
}

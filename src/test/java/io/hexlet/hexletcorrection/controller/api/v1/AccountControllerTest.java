package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.hexlet.hexletcorrection.domain.Account;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void getAllAccountsTest() {
        createCorrection(createAccount(DEFAULT_USER_NAME, "getAllAccounts@mail.com"));
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test(expected = NumberFormatException.class)
    public void getAllAccountsRecursionInfiniteTest() {
        createCorrection(createAccount(DEFAULT_USER_NAME, "getAllAccountsRecursionInfinite@mail.com"));
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract().body().jsonPath().getLong("corrections.account.id");
        assertThatExceptionOfType(StackOverflowError.class);
    }

    @Test
    public void getAccountByIdTest() {
        Account account = createAccount(DEFAULT_USER_NAME, "getAccountById@mail.com");

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + account.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAccountByFalseIdTest() {
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + 1000L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("account", equalTo("Account with ID '" + 1000L + "' not found"));
    }

    @Test
    public void getAccountByNameTest() {
        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/?name=" + DEFAULT_USER_NAME)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void getAccountByFalseNameTest() {
        String falseName = DEFAULT_USER_NAME + "A";

        given().when()
                .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/?name=" + falseName)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);
    }

    @Test
    public void postAccountTest() {
        Account account = Account.builder()
                .name(DEFAULT_USER_NAME)
                .email("postAccount@hexlet.io")
                .build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postAccountDoubleTest() {
        Account account = Account.builder()
                .name(DEFAULT_USER_NAME)
                .email("postAccountDouble@hexlet.io")
                .build();

        given().when()
                .body(account)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());

        Account account2 = Account.builder()
                .name(DEFAULT_USER_NAME)
                .email("postAccountDouble@hexlet.io")
                .build();

        given().when()
                .body(account2)
                .contentType(ContentType.JSON)
                .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void postAccountNameEmptyTest() {
        Account account = Account.builder()
                .email("postAccountNameEmpty@hexlet.io")
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
        Account account = createAccount(DEFAULT_USER_NAME, "deleteAccount@mail.com");

        given().when()
                .delete(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + account.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}

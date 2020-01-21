package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.dto.AccountPostDto;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.TEST_HOST;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.INVALID_EMAIL;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.MAX_ACCOUNT_NAME;
import static io.hexlet.hexletcorrection.domain.EntityConstrainConstants.NOT_EMPTY;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    private static long EXISTED_USER_ID = 1L;

    private static long NON_EXISTED_USER_ID = 1000L;

    @Before
    public void init() {
        when(accountService.findByEmail(any())).thenReturn(Optional.of(createAccount(DEFAULT_USER_NAME, "testCorrections@mail.com")));
        when(accountService.findById(EXISTED_USER_ID)).thenReturn(Optional.of(createAccount(DEFAULT_USER_NAME, "testCorrections@mail.com")));
        when(accountService.findByEmail(any())).thenReturn(Optional.of(createAccount(DEFAULT_USER_NAME, "testCorrections@mail.com")));
    }

    @Test
    public void getAllAccountsTest() {
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON);
    }

    @Test(expected = NumberFormatException.class)
    public void getAllAccountsRecursionInfiniteTest() {
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
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + EXISTED_USER_ID)
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON);
    }

    @Test
    public void getAccountByFalseIdTest() {
        given().when()
            .get(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + NON_EXISTED_USER_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .contentType(ContentType.JSON)
            .body("account", equalTo("Account with ID '" + NON_EXISTED_USER_ID + "' not found"));
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
        AccountPostDto account = AccountPostDto.builder()
            .name(DEFAULT_USER_NAME)
            .email("postAccount@hexlet.io")
            .password(DEFAULT_USER_PASSWORD)
            .passwordConfirm(DEFAULT_USER_PASSWORD)
            .build();

        given().when()
            .body(account)
            .contentType(ContentType.JSON)
            .post(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH)
            .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void postAccountNameEmptyTest() {
        Account account = Account.builder()
            .email("postAccountNameEmpty@hexlet.io")
            .password(DEFAULT_USER_PASSWORD)
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
            .password(DEFAULT_USER_PASSWORD)
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
            .password(DEFAULT_USER_PASSWORD)
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
            .password(DEFAULT_USER_PASSWORD)
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
        doNothing().when(accountService).delete(any());
        given().when()
            .delete(TEST_HOST + ":" + port + API_PATH_V1 + ACCOUNTS_PATH + "/" + EXISTED_USER_ID)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}

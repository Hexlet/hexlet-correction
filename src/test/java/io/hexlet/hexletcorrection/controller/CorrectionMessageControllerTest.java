package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.domain.User;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
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
public class CorrectionMessageControllerTest {

    @Autowired
    CorrectionMessageService correctionMessageService;

    @Autowired
    UserService userService;

    private final static String HOST = "http://localhost";

    private final static String CORRECTION_MESSAGE_PATH = "/correction-message";

    @LocalServerPort
    private int port;

    @Test
    public void getAllMessagesTest() {
        given().when().get(HOST + ":" + port + CORRECTION_MESSAGE_PATH).then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getMessageByIdTest() {
        CorrectionMessage savedMessage = createMessage(createUser());

        given().when().get(HOST + ":" + port + CORRECTION_MESSAGE_PATH + "/" + savedMessage.getId())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getMessageByUrlTest() {
        CorrectionMessage savedMessage = createMessage(createUser());
        savedMessage.setPageURL("hexlet.io");

        given().when().get(HOST + ":" + port + CORRECTION_MESSAGE_PATH + "/?url=" + savedMessage.getPageURL())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void postMessageTest() {
        User user = createUser();

        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .user(user)
                .build();

        given().when().body(correctionMessage).contentType(ContentType.JSON)
                .post(HOST + ":" + port + CORRECTION_MESSAGE_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deleteMessageTest() {
        CorrectionMessage savedMessage = createMessage(createUser());

        given().when().delete(HOST + ":" + port + CORRECTION_MESSAGE_PATH + "/" + savedMessage.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    private User createUser() {
        User user = User.builder().name("Artem").email("artem@hexlet.io").build();

        return userService.create(user);
    }

    private CorrectionMessage createMessage(User user) {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .user(createUser())
                .build();

        return correctionMessageService.create(correctionMessage);
    }
}

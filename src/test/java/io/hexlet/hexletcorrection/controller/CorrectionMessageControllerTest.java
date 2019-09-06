package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
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

    private final static String HOST = "http://localhost";

    @LocalServerPort
    private int port;

    @Test
    public void getAllMessagesTest() {
        given().when().get(HOST + ":" + port + "/correction/list").then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getMessageByIdTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage);

        given().when().get(HOST + ":" + port + "/correction/message/" + savedMessage.getId())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getMessageByUsernameTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage);

        given().when().get(HOST + ":" + port + "/correction/user/" + savedMessage.getUsername())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void getMessageByUrlTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage);

        given().when().get(HOST + ":" + port + "/correction/url/" + savedMessage.getPageURL())
                .then().statusCode(HttpStatus.OK.value()).contentType(ContentType.JSON);
    }

    @Test
    public void postMessageTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        given().when().body(correctionMessage).contentType(ContentType.JSON)
                .post(HOST + ":" + port + "/correction/post")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void deleteMessageTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage);

        given().when().delete(HOST + ":" + port + "/correction/message/" + savedMessage.getId())
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}

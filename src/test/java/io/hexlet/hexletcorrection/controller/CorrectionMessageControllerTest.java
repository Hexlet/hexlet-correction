package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CorrectionMessageControllerTest {

    @Autowired
    CorrectionMessageService correctionMessageService;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllMessagesTest() {
        webTestClient.get().uri("/correction/list")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CorrectionMessage.class);
    }

    @Test
    public void getMessageByIdTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage).block();

        webTestClient.get()
                .uri("/correction/message/{id}", Collections.singletonMap("id", savedMessage.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void getMessageByUsernameTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage).block();

        webTestClient.get()
                .uri("/correction/user/{username}", Collections.singletonMap("username", savedMessage.getUsername()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void getMessageByUrlTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage).block();

        webTestClient.get()
                .uri("/correction/url/{url}", Collections.singletonMap("url", savedMessage.getPageURL()))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void postMessageTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .id("1")
                .build();

        webTestClient.post().uri("/correction/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(correctionMessage), CorrectionMessage.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.comment").isEqualTo("test comment")
                .jsonPath("$.highlightText").isEqualTo("text to correction")
                .jsonPath("$.pageURL").isEqualTo("hexlet.io")
                .jsonPath("$.username").isEqualTo("Artem");
    }

    @Test
    public void deleteMessageTest() {
        CorrectionMessage correctionMessage = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();

        CorrectionMessage savedMessage = correctionMessageService.save(correctionMessage).block();

        webTestClient.delete()
                .uri("correction/message/{id}", Collections.singletonMap("id", savedMessage.getId()))
                .exchange()
                .expectStatus().isOk();
    }
}

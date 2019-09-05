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
    CorrectionMessageService corrMsgServ;
    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    public void getAllMsg() {
        webTestClient.get().uri("/correction/list")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(CorrectionMessage.class);
    }
    
    @Test
    public void getById() {
        CorrectionMessage corrMsg = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();
        
        CorrectionMessage savedMsg = corrMsgServ.save(corrMsg).block();
        
        webTestClient.get()
                .uri("/correction/msg/{id}", Collections.singletonMap("id", savedMsg.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }
    
    @Test
    public void getByUsername() {
        CorrectionMessage corrMsg = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();
        
        CorrectionMessage savedMsg = corrMsgServ.save(corrMsg).block();
        
        webTestClient.get()
                .uri("/correction/user/{username}", Collections.singletonMap("username", savedMsg.getUsername()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }
    
    @Test
    public void getByURL() {
        CorrectionMessage corrMsg = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();
        
        CorrectionMessage savedMsg = corrMsgServ.save(corrMsg).block();
        
        webTestClient.get()
                .uri("/correction/url/{url}", Collections.singletonMap("url", savedMsg.getPageURL()))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .consumeWith(response -> assertThat(response.getResponseBody()).isNotNull());
    }
    
    @Test
    public void postMsg() {
        CorrectionMessage corrMsg = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .id("1")
                .build();
        
        webTestClient.post().uri("/correction/post")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(corrMsg), CorrectionMessage.class)
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
    public void deleteMsg() {
        CorrectionMessage corrMsg = CorrectionMessage.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .username("Artem")
                .build();
        
        CorrectionMessage savedMsg = corrMsgServ.save(corrMsg).block();
        
        webTestClient.delete()
                .uri("correction/msg/{id}", Collections.singletonMap("id", savedMsg.getId()))
                .exchange()
                .expectStatus().isOk();
    }
}
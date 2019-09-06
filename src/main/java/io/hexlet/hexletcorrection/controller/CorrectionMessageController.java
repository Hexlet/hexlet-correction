package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/correction")
@RequiredArgsConstructor
public class CorrectionMessageController {

    private final CorrectionMessageService correctionMessageService;

    @GetMapping(path = "/list")
    public Flux<CorrectionMessage> getAllMessages() {
        return correctionMessageService.findAll();
    }

    @GetMapping(path = "/message/{id}")
    public Mono<CorrectionMessage> getMessageById(@PathVariable("id") String id) {
        return correctionMessageService.findById(id);
    }

    @GetMapping(path = "/user/{username}")
    public Flux<CorrectionMessage> getMessageByUsername(@PathVariable("username") String username) {
        return correctionMessageService.findByUsername(username);
    }

    @GetMapping(path = "/url/{url}")
    public Flux<CorrectionMessage> getMessageByUrl(@PathVariable("url") String url) {
        return correctionMessageService.findByURL(url);
    }

    @PostMapping("/post")
    public Mono<CorrectionMessage> postMessage(@Valid @RequestBody CorrectionMessage correctionMessage) {
        return correctionMessageService.save(correctionMessage);
    }

    @DeleteMapping(path = "/message/{id}")
    public Mono<Void> deleteMessage(@PathVariable("id") String id) {
        return correctionMessageService.delete(id);
    }
}

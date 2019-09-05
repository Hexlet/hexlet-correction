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
    
    private final CorrectionMessageService corrMsgServ;
    
    @GetMapping(path = "/list")
    public Flux<CorrectionMessage> getAllMsg() {
        return corrMsgServ.findAll();
    }
    
    @GetMapping(path = "/msg/{id}")
    public Mono<CorrectionMessage> getById(@PathVariable("id") String id) {
        return corrMsgServ.findById(id);
    }
    
    @GetMapping(path = "/user/{username}")
    public Flux<CorrectionMessage> getByUsername(@PathVariable("username") String username) {
        return corrMsgServ.findByUsername(username);
    }
    
    @GetMapping(path = "/url/{url}")
    public Flux<CorrectionMessage> getByURL(@PathVariable("url") String url) {
        return corrMsgServ.findByURL(url);
    }
    
    @PostMapping("/post")
    public Mono<CorrectionMessage> postMsg(@Valid @RequestBody CorrectionMessage correctionMessage) {
        return corrMsgServ.save(correctionMessage);
    }
    
    @DeleteMapping(path = "/msg/{id}")
    public Mono<CorrectionMessage> deleteMsg(@PathVariable("id") String id) {
        return corrMsgServ.delete(id);
    }
}
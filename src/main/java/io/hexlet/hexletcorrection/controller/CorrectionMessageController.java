package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.CorrectionMessage;
import io.hexlet.hexletcorrection.service.CorrectionMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/correction")
@RequiredArgsConstructor
public class CorrectionMessageController {

    private final CorrectionMessageService correctionMessageService;

    @GetMapping(path = "/list")
    public List<CorrectionMessage> getAllMessages() {
        return correctionMessageService.findAll();
    }

    @GetMapping(path = "/message/{id}")
    public Optional<CorrectionMessage> getMessageById(@PathVariable("id") Long id) {
        return correctionMessageService.findById(id);
    }

    @GetMapping(path = "/user/{username}")
    public List<CorrectionMessage> getMessageByUsername(@PathVariable("username") String username) {
        return correctionMessageService.findByUsername(username);
    }

    @GetMapping(path = "/url/{url}")
    public List<CorrectionMessage> getMessageByUrl(@PathVariable("url") String url) {
        return correctionMessageService.findByURL(url);
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public CorrectionMessage postMessage(@Valid @RequestBody CorrectionMessage correctionMessage) {
        return correctionMessageService.save(correctionMessage);
    }

    @DeleteMapping(path = "/message/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("id") Long id) {
        correctionMessageService.delete(id);
    }
}

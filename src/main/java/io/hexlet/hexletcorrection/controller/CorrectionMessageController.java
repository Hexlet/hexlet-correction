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
@RequestMapping(path = "/correction-message")
@RequiredArgsConstructor
public class CorrectionMessageController {

    private final CorrectionMessageService correctionMessageService;

    @GetMapping
    public List<CorrectionMessage> getMessages(@RequestParam(required = false) String url) {
        if (url == null) {
            return correctionMessageService.findAll();
        }

        return correctionMessageService.findByURL(url);
    }

    @GetMapping(path = "/{id}")
    public Optional<CorrectionMessage> getMessageById(@PathVariable("id") Long id) {
        return correctionMessageService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CorrectionMessage createMessage(@Valid @RequestBody CorrectionMessage correctionMessage) {
        return correctionMessageService.create(correctionMessage);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable("id") Long id) {
        correctionMessageService.delete(id);
    }
}

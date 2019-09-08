package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.service.CorrectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/corrections")
@RequiredArgsConstructor
public class CorrectionController {

    private final CorrectionService correctionService;

    @GetMapping
    public List<Correction> getCorrections(@RequestParam(required = false) String url) {
        if (url == null) {
            return correctionService.findAll();
        }

        return correctionService.findByURL(url);
    }

    @GetMapping(path = "/{id}")
    public Optional<Correction> getCorrectionById(@PathVariable("id") Long id) {
        return correctionService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Correction createCorrection(@Valid @RequestBody Correction correction) {
        return correctionService.create(correction);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCorrection(@PathVariable("id") Long id) {
        correctionService.delete(id);
    }
}

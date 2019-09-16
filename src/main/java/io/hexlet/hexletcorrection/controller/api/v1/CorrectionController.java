package io.hexlet.hexletcorrection.controller.api.v1;

import io.hexlet.hexletcorrection.controller.exception.CorrectionNotFoundException;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import io.hexlet.hexletcorrection.dto.mapper.CorrectionMapper;
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
import java.util.stream.Collectors;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.API_PATH_V1;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.CORRECTIONS_PATH;

@RestController
@RequestMapping(API_PATH_V1 + CORRECTIONS_PATH)
@RequiredArgsConstructor
public class CorrectionController {

    private final CorrectionService correctionService;
    private final CorrectionMapper correctionMapper;

    @GetMapping
    public List<CorrectionDto> getCorrections(@RequestParam(required = false) String url) {
        if (url == null) {
            return correctionService.findAll()
                    .stream()
                    .map(correctionMapper::toCorrectionDto)
                    .collect(Collectors.toList());
        }

        return correctionService.findByURL(url)
                .stream()
                .map(correctionMapper::toCorrectionDto)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/{id}")
    public CorrectionDto getCorrectionById(@PathVariable("id") Long id) {
        return correctionMapper.toCorrectionDto(
                correctionService
                        .findById(id)
                        .orElseThrow(() -> new CorrectionNotFoundException(id))
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CorrectionDto createCorrection(@Valid @RequestBody CorrectionDto correction) {
        correction.setId(null);
        return correctionMapper.toCorrectionDto(
                correctionService.create(
                        correctionMapper.toCorrection(correction)
                )
        );
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCorrection(@PathVariable("id") Long id) {
        correctionService.delete(id);
    }
}

package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.typo.Typo;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.dto.typo.*;
import io.hexlet.typoreporter.web.exception.TypoNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.*;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping(Routers.API_TYPOS)
@RequiredArgsConstructor
public class TypoApi {

    private final TypoService service;

    @PostMapping
    ResponseEntity<ReportedTypo> addTypoReport(@Valid @RequestBody TypoReport typoReport, UriComponentsBuilder builder) {
        var reportedTypo = service.addTypoReport(typoReport);
        var uri = builder.path(Routers.TYPOS).pathSegment(reportedTypo.id().toString()).build().toUri();
        return created(uri).body(reportedTypo);
    }

    @GetMapping
    Page<Typo> getPageTypo(@SortDefault(TYPO_SORT) Pageable pageable) {
        return service.getTypoPage(pageable);
    }

    @GetMapping(ID)
    Typo getTypoById(@PathVariable Long id) {
        return service.getTypoById(id).orElseThrow(() -> new TypoNotFoundException(id));
    }

    @PatchMapping(ID)
    Typo patchTypo(@PathVariable Long id, @Valid @RequestBody PatchTypo patchTypo) {
        return service.patchTypoById(id, patchTypo).orElseThrow(() -> new TypoNotFoundException(id));
    }

    @DeleteMapping(ID)
    void deleteTypoById(@PathVariable Long id) {
        if (service.deleteTypoById(id) == 0) {
            throw new TypoNotFoundException(id);
        }
    }
}

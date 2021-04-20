package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.*;
import io.hexlet.typoreporter.service.dto.typo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.*;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping(API_WORKSPACES)
@RequiredArgsConstructor
public class WorkspaceApi {

    private final TypoService service;

    @PostMapping(WKS_NAME_PATH + TYPOS)
    ResponseEntity<ReportedTypo> addTypoReport(@PathVariable String wksName,
                                               @Valid @RequestBody TypoReport typoReport,
                                               UriComponentsBuilder builder) {
        final var uri = builder.path(WORKSPACE).pathSegment(wksName).path(TYPOS).build().toUri();
        return created(uri).body(service.addTypoReport(typoReport, wksName));
    }
}

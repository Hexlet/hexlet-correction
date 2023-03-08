package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceApi {

    private final TypoService service;

    @PostMapping("/{wksName}/typos")
    public ResponseEntity<ReportedTypo> addTypoReport(@PathVariable String wksName,
                                                      @Valid @RequestBody TypoReport typoReport,
                                                      UriComponentsBuilder builder) {
        final var uri = builder.path("/workspace").pathSegment(wksName).path("/typos").build().toUri();
        return created(uri).body(service.addTypoReport(typoReport, wksName));
    }
}

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

import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.API_WORKSPACES;
import static io.hexlet.typoreporter.web.Routers.Workspace.WKS_NAME_PATH;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping(API_WORKSPACES)
@RequiredArgsConstructor
public class WorkspaceApi {

    private final TypoService service;

    @PostMapping(WKS_NAME_PATH + TYPOS)
    public ResponseEntity<ReportedTypo> addTypoReport(@PathVariable String wksName,
                                                      @Valid @RequestBody TypoReport typoReport,
                                                      UriComponentsBuilder builder) {
        final var uri = builder.path(WORKSPACE).pathSegment(wksName).path(TYPOS).build().toUri();
        return created(uri).body(service.addTypoReport(typoReport, wksName));
    }
}

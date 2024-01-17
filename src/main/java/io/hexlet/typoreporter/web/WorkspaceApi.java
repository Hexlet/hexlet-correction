package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/{id}/typos")
    public ResponseEntity<ReportedTypo> addTypoReport(@PathVariable long id,
                                                      Authentication authentication,
                                                      @Valid @RequestBody TypoReport typoReport,
                                                      UriComponentsBuilder builder) {
        final var wksIdStr = authentication.getName();

        try {
            final var authId = Long.parseLong(wksIdStr);
            if (authId != id) {
                throw new WorkspaceNotFoundException(id);
            }
            final var uri = builder.path("/workspace").pathSegment(wksIdStr).path("/typos").build().toUri();
            return created(uri).body(service.addTypoReport(typoReport, id));
        } catch (NumberFormatException e) {
            throw new WorkspaceNotFoundException(wksIdStr, e);
        }
    }
}

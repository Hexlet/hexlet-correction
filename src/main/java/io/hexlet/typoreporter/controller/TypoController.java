package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.domain.typo.TypoEvent;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.handler.exception.TypoNotFoundException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

//TODO add tests
@Slf4j
@Controller
@RequestMapping("/typos")
@RequiredArgsConstructor
public class TypoController {

    private final TypoService typoService;

    private final WorkspaceService workspaceService;

    @PatchMapping("/{id}/status")
    public String updateTypoStatus(@PathVariable Long id,
                                   @RequestParam Optional<Long> wksId,
                                   @RequestParam Optional<TypoEvent> event) {
        if (wksId.isEmpty() || !workspaceService.existsWorkspaceById(wksId.get())) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksId.orElse(0L));
            log.error(e.toString(), e);
            return "redirect:/workspaces";
        }
        if (event.isEmpty()) {
            //TODO send to error page
            log.error("TypoEvent={} must not be null", event.orElse(null));
            return ("redirect:/workspace/") + wksId.get() + "/typos";
        }
        final var updatedTypo = typoService.updateTypoStatus(id, event.get());
        if (updatedTypo.isEmpty()) {
            //TODO send to error page
            final var e = new TypoNotFoundException(id);
            log.error(e.getMessage(), e);
        }
        return ("redirect:/workspace/") + wksId.get() + "/typos";
    }

    @DeleteMapping("/{id}")
    public String deleteTypoById(@PathVariable Long id,
                                 @RequestParam Optional<Long> wksId) {
        if (wksId.isEmpty() || !workspaceService.existsWorkspaceById(wksId.get())) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksId.orElse(0L));
            log.error(e.toString(), e);
            return "redirect:/workspaces";
        }
        if (typoService.deleteTypoById(id) == 0) {
            //TODO send to error page
            final var e = new TypoNotFoundException(id);
            log.error(e.toString(), e);
        }
        return ("redirect:/workspace/") + wksId.get() + "/typos";
    }
}

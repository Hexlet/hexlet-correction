package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.typo.TypoEvent;
import io.hexlet.typoreporter.service.*;
import io.hexlet.typoreporter.web.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Routers.Typo.*;
import static io.hexlet.typoreporter.web.Routers.Workspace.REDIRECT_WKS_ROOT;

//TODO add tests
@Slf4j
@Controller
@RequestMapping(TYPOS)
@RequiredArgsConstructor
public class TypoController {

    private final TypoService typoService;

    private final WorkspaceService workspaceService;

    @PatchMapping(ID_PATH + TYPO_STATUS)
    String updateTypoStatus(@PathVariable Long id,
                            @RequestParam Optional<String> wksName,
                            @RequestParam Optional<TypoEvent> event) {
        if (wksName.isEmpty() || !workspaceService.existsWorkspaceByName(wksName.get())) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksName.orElse(""));
            log.error(e.toString(), e);
            return REDIRECT_ROOT;
        }
        if (event.isEmpty()) {
            //TODO send to error page
            log.error("TypoEvent={} must not be null", event.orElse(null));
            return REDIRECT_WKS_ROOT + wksName.get() + TYPOS;
        }
        final var updatedTypo = typoService.updateTypoStatus(id, event.get());
        if (updatedTypo.isEmpty()) {
            //TODO send to error page
            final var e = new TypoNotFoundException(id);
            log.error(e.getMessage(), e);
        }
        return REDIRECT_WKS_ROOT + wksName.get() + TYPOS;
    }

    @DeleteMapping(ID_PATH)
    String deleteTypoById(@PathVariable Long id,
                          @RequestParam Optional<String> wksName) {
        if (wksName.isEmpty() || !workspaceService.existsWorkspaceByName(wksName.get())) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksName.orElse(""));
            log.error(e.toString(), e);
            return REDIRECT_ROOT;
        }
        if (typoService.deleteTypoById(id) == 0) {
            //TODO send to error page
            final var e = new TypoNotFoundException(id);
            log.error(e.toString(), e);
        }
        return REDIRECT_WKS_ROOT + wksName.get() + TYPOS;
    }
}

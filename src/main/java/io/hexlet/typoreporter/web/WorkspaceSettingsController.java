package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.WorkspaceSettingsService;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.UUID;
import static io.hexlet.typoreporter.web.Routers.REDIRECT_ROOT;
import static io.hexlet.typoreporter.web.Routers.SETTINGS;
import static io.hexlet.typoreporter.web.Routers.Workspace.*;
import static io.hexlet.typoreporter.web.Templates.WKS_SETTINGS_TEMPLATE;

@Slf4j
@Controller
@RequestMapping(WORKSPACE + WKS_NAME_PATH)
@RequiredArgsConstructor
public class WorkspaceSettingsController {

    private final TypoService typoService;
    private final WorkspaceService workspaceService;
    private final WorkspaceSettingsService workspaceSettingsService;

    @GetMapping(SETTINGS)
    public String getWorkspaceSettingsPage(Model model, @PathVariable String wksName) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        model.addAttribute("wksName", wksName);

        final var wksToken = workspaceSettingsService.getWorkspaceApiAccessTokenByName(wksName);
        model.addAttribute("wksToken", wksToken);

        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return WKS_SETTINGS_TEMPLATE;
    }

    @PatchMapping("/token/regenerate")
    public String patchWorkspaceToken(@PathVariable String wksName) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        workspaceSettingsService.regenerateWorkspaceApiAccessTokenByName(wksName);
        return REDIRECT_WKS_ROOT + wksName + SETTINGS;
    }

    private void getStatisticDataToModel(final Model model, final String wksName) {
        final var countTypoByStatus = typoService.getCountTypoByStatusForWorkspaceName(wksName);
        model.addAttribute("countTypoByStatus", countTypoByStatus);
        model.addAttribute("sumTypoInWks", countTypoByStatus.stream().mapToLong(Pair::getValue).sum());
    }

    private void getLastTypoDataToModel(final Model model, final String wksName) {
        final var createdDate = typoService.getLastTypoByWorkspaceName(wksName).map(TypoInfo::createdDate);
        model.addAttribute("lastTypoCreatedDate", createdDate);
        model.addAttribute("lastTypoCreatedDateAgo", createdDate.map(new PrettyTime()::format));
    }
}

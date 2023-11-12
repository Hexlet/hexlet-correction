package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.WorkspaceSettingsService;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping("/workspace/{wksName}")
@RequiredArgsConstructor
public class WorkspaceSettingsController {

    private final TypoService typoService;
    private final WorkspaceService workspaceService;
    private final WorkspaceSettingsService workspaceSettingsService;
    private final AccountService accountService;

    @GetMapping("/settings")
    public String getWorkspaceSettingsPage(Model model, @PathVariable String wksName, HttpServletRequest req) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        model.addAttribute("wksName", wksName);

        addTokenAndUrlToModel(model, wksName, req);

        final Account authenticatedAccount = getAccountFromAuthentication();
        final boolean accountIsAdminRole = workspaceService.isAdminRoleUserInWorkspace(wksName,
            authenticatedAccount.getUsername());
        model.addAttribute("isAdmin", accountIsAdminRole);

        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return "workspace/wks-settings";
    }

    @GetMapping("/integration")
    public String getWorkspaceIntegrationPage(Model model, @PathVariable String wksName, HttpServletRequest req) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        model.addAttribute("wksName", wksName);

        addTokenAndUrlToModel(model, wksName, req);

        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return "workspace/wks-integration";
    }

    @PatchMapping("/token/regenerate")
    public String patchWorkspaceToken(@PathVariable String wksName) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        workspaceSettingsService.regenerateWorkspaceApiAccessTokenByName(wksName);
        return ("redirect:/workspace/") + wksName + "/settings";
    }

    private void getStatisticDataToModel(final Model model, final String wksName) {
        final var countTypoByStatus = typoService.getCountTypoByStatusForWorkspaceName(wksName);
        model.addAttribute("countTypoByStatus", countTypoByStatus);
        model.addAttribute("sumTypoInWks", countTypoByStatus.stream().mapToLong(Pair::getValue).sum());
    }

    private void getLastTypoDataToModel(final Model model, final String wksName) {
        final var createdDate = typoService.getLastTypoByWorkspaceName(wksName).map(TypoInfo::createdDate);
        model.addAttribute("lastTypoCreatedDate", createdDate);
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("lastTypoCreatedDateAgo", createdDate.map(new PrettyTime(locale)::format));
    }

    private void addTokenAndUrlToModel(Model model, String wksName, HttpServletRequest req) {
        final var settings = workspaceSettingsService.getWorkspaceSettingsByWorkspaceName(wksName);
        final var basicTokenStr = settings.getId() + ":" + settings.getApiAccessToken();
        final var wksBasicToken = Base64.getEncoder().encodeToString(basicTokenStr.getBytes());
        model.addAttribute("wksBasicToken", wksBasicToken);
        final var rootUrl = req.getRequestURL().toString().replace(req.getRequestURI(), "");
        model.addAttribute("rootUrl", rootUrl);
        final var wksId = settings.getWorkspace().getId();
        model.addAttribute("wksId", wksId);
    }

    private Account getAccountFromAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountService.findByUsername(authentication.getName());
    }
}

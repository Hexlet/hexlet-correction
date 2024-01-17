package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.WorkspaceSettingsService;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static io.hexlet.typoreporter.controller.WorkspaceController.IS_USER_ADMIN_IN_WKS;
import static io.hexlet.typoreporter.controller.WorkspaceController.IS_USER_RELATED_TO_WKS;

@Slf4j
@Controller
@RequestMapping("/workspace/{wksId}")
@RequiredArgsConstructor
public class WorkspaceSettingsController {

    private final TypoService typoService;
    private final WorkspaceService workspaceService;
    private final WorkspaceSettingsService workspaceSettingsService;
    private final AccountService accountService;

    @Transactional
    @GetMapping("/settings")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceSettingsPage(Model model, @PathVariable Long wksId, HttpServletRequest req) {
        if (!workspaceService.existsWorkspaceById(wksId)) {
            //TODO send to error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }

        addTokenAndUrlToModel(model, wksId, req);

        String wksName = workspaceService.getWorkspaceById(wksId).get().getName();
        model.addAttribute("wksName", wksName);

        final Account authenticatedAccount = getAccountFromAuthentication();
        final boolean accountIsAdminRole = workspaceService.isAdminRoleUserInWorkspace(wksId,
            authenticatedAccount.getEmail());
        model.addAttribute("isAdmin", accountIsAdminRole);

        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);
        return "workspace/wks-settings";
    }

    @GetMapping("/integration")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceIntegrationPage(Model model, @PathVariable Long wksId, HttpServletRequest req) {
        if (!workspaceService.existsWorkspaceById(wksId)) {
            //TODO send to error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }

        addTokenAndUrlToModel(model, wksId, req);

        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);

        return "workspace/wks-integration";
    }

    @PatchMapping("/token/regenerate")
    @PreAuthorize(IS_USER_ADMIN_IN_WKS)
    public String patchWorkspaceToken(@PathVariable Long wksId) {
        if (!workspaceService.existsWorkspaceById(wksId)) {
            //TODO send to error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }
        workspaceSettingsService.regenerateWorkspaceApiAccessTokenById(wksId);
        return ("redirect:/workspace/") + wksId.toString() + "/settings";
    }

    private void getStatisticDataToModel(final Model model, final Long wksId) {
        final var countTypoByStatus = typoService.getCountTypoByStatusForWorkspaceId(wksId);
        model.addAttribute("countTypoByStatus", countTypoByStatus);
        model.addAttribute("sumTypoInWks", countTypoByStatus.stream().mapToLong(Pair::getValue).sum());
    }

    private void getLastTypoDataToModel(final Model model, final Long wksId) {
        final var createdDate = typoService.getLastTypoByWorkspaceId(wksId).map(TypoInfo::createdDate);
        model.addAttribute("lastTypoCreatedDate", createdDate);
        Locale locale = LocaleContextHolder.getLocale();
        model.addAttribute("lastTypoCreatedDateAgo", createdDate.map(new PrettyTime(locale)::format));
    }

    private void addTokenAndUrlToModel(Model model, Long wksId, HttpServletRequest req) {
        final var settings = workspaceSettingsService.getWorkspaceSettingsByWorkspaceId(wksId);
        final var basicTokenStr = settings.getId() + ":" + settings.getApiAccessToken();
        final var wksBasicToken = Base64.getEncoder().encodeToString(basicTokenStr.getBytes());
        model.addAttribute("wksBasicToken", wksBasicToken);
        final var rootUrl = req.getRequestURL().toString().replace(req.getRequestURI(), "");
        model.addAttribute("rootUrl", rootUrl);
        model.addAttribute("wksId", wksId);
    }

    private Account getAccountFromAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountService.findByEmail(authentication.getName());
    }
}

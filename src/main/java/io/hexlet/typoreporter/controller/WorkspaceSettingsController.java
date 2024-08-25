package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.AllowedUrl;
import io.hexlet.typoreporter.handler.exception.AllowedUrlAlreadyExistException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.WorkspaceSettingsService;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.workspace.AllowedUrlDTO;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import static io.hexlet.typoreporter.controller.WorkspaceController.IS_USER_ADMIN_IN_WKS;
import static io.hexlet.typoreporter.controller.WorkspaceController.IS_USER_RELATED_TO_WKS;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order.asc;

@Slf4j
@Controller
@RequestMapping("/workspace/{wksId}")
@RequiredArgsConstructor
public class WorkspaceSettingsController {

    private final TypoService typoService;
    private final WorkspaceService workspaceService;
    private final WorkspaceSettingsService workspaceSettingsService;
    private final AccountService accountService;

    private final TreeSet<Integer> availableSizes = new TreeSet<>(List.of(2, 5, 10, 15, 25));

    @Transactional
    @GetMapping("/settings")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceSettingsPage(Model model,
                                           @PathVariable Long wksId,
                                           HttpServletRequest req,
                                           @SortDefault("url") Pageable pageable) {
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

        Page<AllowedUrl> urlsPage = workspaceSettingsService.getPagedAllowedUrlsByWorkspaceId(pageable, wksId);

        var sort = urlsPage.getSort()
            .stream()
            .findFirst()
            .orElseGet(() -> asc("url"));

        if (!model.containsAttribute("inputUrl")) {
            model.addAttribute("inputUrl", new AllowedUrlDTO(""));
        }

        model.addAttribute("sortDir", sort.getDirection());
        model.addAttribute("DESC", DESC);
        model.addAttribute("ASC", ASC);
        model.addAttribute("formModified", false);
        model.addAttribute("urlsPage", urlsPage);
        model.addAttribute("availableSizes", availableSizes);

        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);
        return "workspace/wks-settings";
    }

    @PostMapping("/allowed-urls")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String postAllowedUrl(Model model,
                                 @PathVariable Long wksId,
                                 @Valid @ModelAttribute AllowedUrlDTO inputUrl,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        getStatisticDataToModel(model, wksId);
        final String inputUrlAttribute = "org.springframework.validation.BindingResult.inputUrl";

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(inputUrlAttribute, bindingResult);
            redirectAttributes.addFlashAttribute("inputUrl", inputUrl);
            redirectAttributes.addFlashAttribute("formModified", true);

            return "redirect:/workspace/{wksId}/settings";
        }
        try {
            workspaceSettingsService.addAllowedUrlToWorkspace(wksId, inputUrl.url());
            return "redirect:/workspace/{wksId}/settings";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        } catch (AllowedUrlAlreadyExistException e) {
            log.error("Allowed url {} already exists", inputUrl.url());
            bindingResult.addError(new FieldError("inputUrl",
                                    "url",
                                    inputUrl.url(),
                                    false,
                                    null,
                                    null,
                                    e.getDetailMessageCode()));
            redirectAttributes.addFlashAttribute(inputUrlAttribute, bindingResult);
            redirectAttributes.addFlashAttribute("inputUrl", inputUrl);
            redirectAttributes.addFlashAttribute("formModified", true);

            return "redirect:/workspace/{wksId}/settings";
        }
    }

    @DeleteMapping("/allowed-urls")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String deleteAllowedUrl(Model model,
                                   @PathVariable Long wksId,
                                   @RequestParam String url) {
        getStatisticDataToModel(model, wksId);

        try {
            workspaceSettingsService.removeAllowedUrlFromWorkspace(wksId, url);
            return ("redirect:/workspace/") + wksId.toString() + "/settings";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }
    }

    @GetMapping("/integration")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceIntegrationPage(Model model, @PathVariable Long wksId, HttpServletRequest req) {
        var wksOptional = workspaceService.getWorkspaceInfoById(wksId).orElse(null);

        if (wksOptional == null) {
            //TODO send to error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }

        addTokenAndUrlToModel(model, wksId, req);

        model.addAttribute("wksInfo", wksOptional);
        model.addAttribute("wksName", wksOptional.name());

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

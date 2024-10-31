package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.web.model.WorkspaceUserModel;
import org.springframework.ui.Model;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.typo.TypoStatus;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceRoleService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.handler.exception.AccountNotFoundException;
import io.hexlet.typoreporter.handler.exception.WorkspaceAlreadyExistException;
import io.hexlet.typoreporter.handler.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.handler.exception.WorkspaceRoleNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.domain.Sort.Order.asc;

@Slf4j
@Controller
@RequestMapping("/workspace")
@RequiredArgsConstructor
public class WorkspaceController {
    protected static final String IS_USER_RELATED_TO_WKS =
        "@workspaceService.isUserRelatedToWorkspace(#wksId, authentication.name)";

    protected static final String IS_USER_ADMIN_IN_WKS =
        "@workspaceService.isAdminRoleUserInWorkspace(#wksId, authentication.name)";

    private final TreeSet<Integer> availableSizes = new TreeSet<>(List.of(2, 5, 10, 15, 25));

    private final List<TypoStatus> availableStatuses = Arrays.asList(TypoStatus.values());
//        .stream()
//        .map(status -> status.name().replace("_"," "))
//        .collect(Collectors.toList());

    private final TypoService typoService;

    private final WorkspaceService workspaceService;

    private final AccountService accountService;

    private final WorkspaceRoleService workspaceRoleService;

    @GetMapping
    public String getCreateWorkspacePage(final Model model) {
        model.addAttribute("createWorkspace", new CreateWorkspace("", "", ""));
        model.addAttribute("formModified", false);
        return "create-workspace";
    }

    @PostMapping
    public String postCreateWorkspacePage(final Model model,
                                          Authentication principal,
                                          @Valid @ModelAttribute CreateWorkspace createWorkspace,
                                          BindingResult bindingResult) {
        String email = principal.getName();
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", createWorkspace);
            return "create-workspace";
        }
        try {
            workspaceService.createWorkspace(createWorkspace, email);
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("createWorkspace"));
            return "create-workspace";
        }
        return "redirect:/workspaces";
    }

    @GetMapping("/{wksId}")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceInfoPage(Model model, @PathVariable Long wksId) {
        var wksOptional = workspaceService.getWorkspaceInfoById(wksId);
        if (wksOptional.isEmpty()) {
            //TODO send to error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }
        WorkspaceInfo wksInfo = wksOptional.get();
        model.addAttribute("wksInfo", wksInfo);
        model.addAttribute("wksName", wksInfo.name());

        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);
        return "workspace/wks-info";
    }

    @GetMapping("/{wksId}/typos")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceTyposPage(Model model,
                                        @PathVariable Long wksId,
                                        @RequestParam(required = false) String typoStatus,
                                        @SortDefault("createdDate") Pageable pageable) {
        var wksOptional = workspaceService.getWorkspaceInfoById(wksId);
        if (wksOptional.isEmpty()) {
            //TODO send error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }

        WorkspaceInfo wksInfo = wksOptional.get();
        model.addAttribute("wksName", wksInfo.name());
        model.addAttribute("wksInfo", wksInfo);
        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);

        var size = Optional.ofNullable(availableSizes.floor(pageable.getPageSize())).orElseGet(availableSizes::first);
        var pageRequest = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        Page<TypoInfo> typoPage;
        if (typoStatus == null) {
            typoPage = typoService.getTypoPage(pageRequest, wksId);
        } else {
            typoPage = typoService.getTypoPageFiltered(pageRequest, wksId, typoStatus);
            model.addAttribute("typoStatus", typoStatus);
            model.addAttribute("typoStatusStyle", TypoStatus.valueOf(typoStatus).getStyle());
        }

        var sort = typoPage.getSort()
            .stream()
            .findFirst()
            .orElseGet(() -> asc("createdDate"));

        model.addAttribute("typoPage", typoPage);
        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("availableStatuses", availableStatuses);
        model.addAttribute("sortProp", sort.getProperty());
        model.addAttribute("sortDir", sort.getDirection());
        model.addAttribute("DESC", DESC);
        model.addAttribute("ASC", ASC);
        return "workspace/wks-typos";
    }

    @GetMapping("/{wksId}/update")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceUpdatePage(Model model, @PathVariable Long wksId) {
        var wksOptional = workspaceService.getWorkspaceInfoById(wksId);
        if (wksOptional.isEmpty()) {
            //TODO send to error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }
        final var wksUpdate = wksOptional
            .map(wksInfo -> new CreateWorkspace(wksInfo.name(), wksInfo.description(), wksInfo.url()))
            .get();
        model.addAttribute("createWorkspace", wksUpdate);
        model.addAttribute("wksName", wksOptional.get().name());
        model.addAttribute("wksId", wksId);
        model.addAttribute("formModified", false);
        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);
        return "workspace/wks-update";
    }

    //TODO add tests
    @PutMapping("/{wksId}/update")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String putWorkspaceUpdate(Model model,
                                     @PathVariable Long wksId,
                                     @Valid @ModelAttribute CreateWorkspace wksUpdate,
                                     BindingResult bindingResult) {
        model.addAttribute("formModified", true);
        getStatisticDataToModel(model, wksId);
        getLastTypoDataToModel(model, wksId);

        if (bindingResult.hasErrors()) {
            var wksOptional = workspaceService.getWorkspaceInfoById(wksId);
            String wksName = wksOptional.get().name();
            model.addAttribute("wksName", wksName);
            model.addAttribute("createWorkspace", wksUpdate);
            return "workspace/wks-update";
        }
        try {
            workspaceService.updateWorkspace(wksUpdate, wksId);
            return ("redirect:/workspace/") + wksId.toString();
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("createWorkspace"));
            return "workspace/wks-update";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }
    }

    @DeleteMapping("/{wksId}")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String deleteWorkspaceById(@PathVariable Long wksId) {
        if (workspaceService.deleteWorkspaceById(wksId) == 0) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksId);
            log.error(e.toString(), e);
        }
        return "redirect:/workspaces";
    }

    @GetMapping("/{wksId}/users")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceUsersPage(final Model model,
                                        @PathVariable Long wksId,
                                        @SortDefault("createdDate") Pageable pageable) {

        model.addAttribute("inputEmail", new WorkspaceUserModel());
        model.addAttribute("formModified", false);
        Optional<WorkspaceInfo> workSpaceInfo = workspaceService.getWorkspaceInfoById(wksId);
        if (workSpaceInfo.isEmpty()) {
            //TODO send error page
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        }
        prepareDataToRenderPage(model, workSpaceInfo.get(), pageable);
        return "workspace/wks-users";
    }

    @PostMapping("/{wksId}/users")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String addUser(@ModelAttribute("inputEmail") @Valid WorkspaceUserModel workspaceUserModel,
                          BindingResult bindingResult,
                          Model model,
                          @PathVariable Long wksId,
                          @SortDefault("createdDate") Pageable pageable
    ) {
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            Optional<WorkspaceInfo> workspaceInfo = workspaceService.getWorkspaceInfoById(wksId);
            if (workspaceInfo.isEmpty()) {
                //TODO send error page
                log.error("Workspace with id {} not found", wksId);
                return "redirect:/workspaces";
            }
            prepareDataToRenderPage(model, workspaceInfo.get(), pageable);
            return "workspace/wks-users";
        }
        try {
            workspaceRoleService.addAccountToWorkspace(wksId, workspaceUserModel.getEmail());
            return "redirect:/workspace/{wksId}/users";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        } catch (AccountNotFoundException e) {
            bindingResult.addError(new FieldError("inputEmail", "email", e, false, null, null, e.getMessage()));
            log.error("Account with email {} not found", workspaceUserModel.getEmail());
            return "redirect:/workspace/{wksId}/users";
        }
    }

    @DeleteMapping("/{wksId}/users")
    @PreAuthorize(IS_USER_ADMIN_IN_WKS)
    public String deleteUser(@RequestParam String email, @PathVariable Long wksId) {
        try {
            workspaceRoleService.deleteAccountFromWorkspace(wksId, email);
            return "redirect:/workspace/{wksId}/users";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with id {} not found", wksId);
            return "redirect:/workspaces";
        } catch (AccountNotFoundException e) {
            log.error("Account with email {} not found", email);
            return "redirect:/workspace/{wksId}/users";
        } catch (WorkspaceRoleNotFoundException e) {
            log.error("The user with email {} has no role in the workspace with id {} ", email, wksId, e);
            return "redirect:/workspaces";
        }
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

    private List<Account> getNonLinkedAccounts(Collection<Account> allAccounts, Collection<Account> linkedAccounts) {
        final List<Long> linkedIds = linkedAccounts.stream()
            .map(Account::getId)
            .toList();
        return allAccounts.stream()
            .filter(account -> !linkedIds.contains(account.getId()))
            .collect(Collectors.toList());
    }

    private Account getAccountFromAuthentication() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return accountService.findByEmail(authentication.getName());
    }

    /**
     * Preparing data for page rendering /workspace/{id}/users
     * @param model - it supplies attributes that used for rendering page
     * @param workspaceInfo - dto of workspace model
     * @param pageable - abstract interface for pagination page
     */
    private void prepareDataToRenderPage(final Model model,
                                         WorkspaceInfo workspaceInfo,
                                         Pageable pageable) {
        model.addAttribute("wksName", workspaceInfo.name());
        model.addAttribute("wksInfo", workspaceInfo);
        getStatisticDataToModel(model, workspaceInfo.id());
        getLastTypoDataToModel(model, workspaceInfo.id());

        Optional<Workspace> workspaceOptional = workspaceService.getWorkspaceById(workspaceInfo.id());
        Set<WorkspaceRole> workspaceRoles = workspaceOptional.get().getWorkspaceRoles();
        List<Account> linkedAccounts = workspaceRoles.stream()
            .map(WorkspaceRole::getAccount)
            .collect(Collectors.toList());
        List<Account> allAccounts = accountService.findAll();
        List<Account> nonLinkedAccounts = getNonLinkedAccounts(allAccounts, linkedAccounts);
        final Account authenticatedAccount = getAccountFromAuthentication();
        final boolean accountIsAdminRole = workspaceService.isAdminRoleUserInWorkspace(workspaceInfo.id(),
            authenticatedAccount.getEmail());
        List<Account> excludeDeleteAccounts = Collections.singletonList(authenticatedAccount);
        Page<Account> userPage = new PageImpl<>(linkedAccounts, pageable, linkedAccounts.size());
        var sort = userPage.getSort()
            .stream()
            .findFirst()
            .orElseGet(() -> asc("createdDate"));

        model.addAttribute("nonLinkedAccounts", nonLinkedAccounts);
        model.addAttribute("isAdmin", accountIsAdminRole);
        model.addAttribute("excludeDeleteAccounts", excludeDeleteAccounts);
        model.addAttribute("userPage", userPage);
        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("sortProp", sort.getProperty());
        model.addAttribute("sortDir", sort.getDirection());
        model.addAttribute("DESC", DESC);
        model.addAttribute("ASC", ASC);
    }
}

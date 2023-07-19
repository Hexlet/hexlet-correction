package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.domain.workspace.Workspace;
import io.hexlet.typoreporter.domain.workspace.WorkspaceRole;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceRoleService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.service.dto.workspace.WorkspaceInfo;
import io.hexlet.typoreporter.web.exception.AccountNotFoundException;
import io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException;
import io.hexlet.typoreporter.web.exception.WorkspaceNotFoundException;
import io.hexlet.typoreporter.web.exception.WorkspaceRoleNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    private static final String IS_USER_RELATED_TO_WKS =
        "@workspaceService.isUserRelatedToWorkspace(#wksName, authentication.name)";
    private static final String IS_USER_ADMIN_IN_WKS =
        "@workspaceService.isAdminRoleUserInWorkspace(#wksName, authentication.name)";

    private final TreeSet<Integer> availableSizes = new TreeSet<>(List.of(2, 5, 10, 15, 25));

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
                                          Principal principal,
                                          @Valid @ModelAttribute CreateWorkspace createWorkspace,
                                          BindingResult bindingResult) {
        String userName = principal.getName();
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", createWorkspace);
            return "create-workspace";
        }
        try {
            workspaceService.createWorkspace(createWorkspace, userName);
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("createWorkspace"));
            return "create-workspace";
        }
        return "redirect:/workspaces";
    }

    @GetMapping("/{wksName}")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceInfoPage(Model model, @PathVariable String wksName) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        model.addAttribute("wksInfo", wksOptional.get());
        model.addAttribute("wksName", wksName);

        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return "workspace/wks-info";
    }

    @GetMapping("/{wksName}/typos")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceTyposPage(Model model,
                                        @PathVariable String wksName,
                                        @SortDefault("createdDate") Pageable pageable) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        model.addAttribute("wksName", wksName);
        model.addAttribute("wksInfo", wksOptional.get());
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);

        var size = Optional.ofNullable(availableSizes.floor(pageable.getPageSize())).orElseGet(availableSizes::first);
        var pageRequest = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        var typoPage = typoService.getTypoPage(pageRequest, wksName);

        var sort = typoPage.getSort()
            .stream()
            .findFirst()
            .orElseGet(() -> asc("createdDate"));

        model.addAttribute("typoPage", typoPage);
        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("sortProp", sort.getProperty());
        model.addAttribute("sortDir", sort.getDirection());
        model.addAttribute("DESC", DESC);
        model.addAttribute("ASC", ASC);
        return "workspace/wks-typos";
    }

    @GetMapping("/{wksName}/update")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceUpdatePage(Model model, @PathVariable String wksName) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        final var wksUpdate = wksOptional
            .map(wksInfo -> new CreateWorkspace(wksInfo.name(), wksInfo.description(), wksInfo.url()))
            .get();
        model.addAttribute("createWorkspace", wksUpdate);
        model.addAttribute("wksName", wksName);
        model.addAttribute("formModified", false);
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return "workspace/wks-update";
    }

    //TODO add tests
    @PutMapping("/{wksName}/update")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String putWorkspaceUpdate(Model model,
                                     @PathVariable String wksName,
                                     @Valid @ModelAttribute CreateWorkspace wksUpdate,
                                     BindingResult bindingResult) {
        model.addAttribute("wksName", wksName);
        model.addAttribute("formModified", true);
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);

        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", wksUpdate);
            return "workspace/wks-update";
        }
        try {
            if (workspaceService.updateWorkspace(wksUpdate, wksName).isEmpty()) {
                //TODO send to error page
                log.error("Workspace with name {} not found", wksName);
                return "redirect:/workspaces";
            }
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("createWorkspace"));
            return "workspace/wks-update";
        }
        return ("redirect:/workspace/") + wksUpdate.name();
    }

    @DeleteMapping("/{wksName}")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String deleteWorkspaceByName(@PathVariable String wksName) {
        if (workspaceService.deleteWorkspaceByName(wksName) == 0) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksName);
            log.error(e.toString(), e);
        }
        return "redirect:/workspaces";
    }

    @GetMapping("/{wksName}/users")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String getWorkspaceUsersPage(Model model,
                                        @PathVariable String wksName,
                                        @SortDefault("createdDate") Pageable pageable) {

        Optional<WorkspaceInfo> workSpaceInfoOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (workSpaceInfoOptional.isEmpty()) {
            //TODO send error page
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        }
        model.addAttribute("wksName", wksName);
        model.addAttribute("wksInfo", workSpaceInfoOptional.get());
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);

        Optional<Workspace> workspaceOptional = workspaceService.getWorkspaceByName(wksName);
        Set<WorkspaceRole> workspaceRoles = workspaceOptional.get().getWorkspaceRoles();
        List<Account> linkedAccounts = workspaceRoles.stream()
            .map(WorkspaceRole::getAccount)
            .collect(Collectors.toList());
        List<Account> allAccounts = accountService.findAll();
        List<Account> nonLinkedAccounts = getNonLinkedAccounts(allAccounts, linkedAccounts);
        final Account authenticatedAccount = getAccountFromAuthentication();
        final boolean accountIsAdminRole = workspaceService.isAdminRoleUserInWorkspace(wksName,
            authenticatedAccount.getUsername());
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
        return "workspace/wks-users";
    }

    @PostMapping("/{wksName}/users")
    @PreAuthorize(IS_USER_RELATED_TO_WKS)
    public String addUser(@RequestParam String email, @PathVariable String wksName) {
        try {
            workspaceRoleService.addAccountToWorkspace(wksName, email);
            return "redirect:/workspace/{wksName}/users";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        } catch (AccountNotFoundException e) {
            log.error("Account with email {} not found", email);
            return "redirect:/workspace/{wksName}/users";
        }
    }

    @DeleteMapping("/{wksName}/users")
    @PreAuthorize(IS_USER_ADMIN_IN_WKS)
    public String deleteUser(@RequestParam String email, @PathVariable String wksName) {
        try {
            workspaceRoleService.deleteAccountFromWorkspace(wksName, email);
            return "redirect:/workspace/{wksName}/users";
        } catch (WorkspaceNotFoundException e) {
            log.error("Workspace with name {} not found", wksName);
            return "redirect:/workspaces";
        } catch (AccountNotFoundException e) {
            log.error("Account with email {} not found", email);
            return "redirect:/workspace/{wksName}/users";
        } catch (WorkspaceRoleNotFoundException e) {
            log.error("The user with email {} has no role in the workspace {} ", email, wksName, e);
            return "redirect:/workspaces";
        }
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
        return accountService.findByUsername(authentication.getName());
    }
}

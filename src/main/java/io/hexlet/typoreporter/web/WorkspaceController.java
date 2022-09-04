package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.*;
import io.hexlet.typoreporter.service.dto.typo.TypoInfo;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.web.exception.*;
import io.hexlet.typoreporter.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.data.domain.*;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.IntStream;

import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Routers.Typo.TYPOS;
import static io.hexlet.typoreporter.web.Routers.Workspace.*;
import static io.hexlet.typoreporter.web.Templates.*;
import static io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException.fieldNameError;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.data.domain.Sort.Order.asc;

@Slf4j
@Controller
@RequestMapping(WORKSPACE + WKS_NAME_PATH)
@RequiredArgsConstructor
public class WorkspaceController {

    private final TreeSet<Integer> availableSizes = new TreeSet<>(List.of(2, 5, 10, 15, 25));

    private final TypoService typoService;

    private final WorkspaceService workspaceService;

    private final AccountRepository accountRepository;

    @GetMapping
    public String getWorkspaceInfoPage(Model model, @PathVariable String wksName) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        model.addAttribute("wksInfo", wksOptional.get());
        model.addAttribute("wksName", wksName);

        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return WKS_INFO_TEMPLATE;
    }

    @GetMapping(SETTINGS)
    public String getWorkspaceSettingsPage(Model model, @PathVariable String wksName) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        model.addAttribute("wksName", wksName);
        final var wksToken = workspaceService.getWorkspaceApiAccessTokenByName(wksName)
            .map(UUID::toString);
        if (wksToken.isEmpty()) {
            log.error("Workspace with name {} not found or token not generated", wksName);
        }
        model.addAttribute("wksToken", wksToken.orElse(""));

        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return WKS_SETTINGS_TEMPLATE;
    }

    @GetMapping(TYPOS)
    public String getWorkspaceTyposPage(Model model,
                                        @PathVariable String wksName,
                                        @SortDefault(DEFAULT_SORT_FIELD) Pageable pageable) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
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
            .orElseGet(() -> asc(DEFAULT_SORT_FIELD));

        model.addAttribute("typoPage", typoPage);
        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("sortProp", sort.getProperty());
        model.addAttribute("sortDir", sort.getDirection());
        model.addAttribute("DESC", DESC);
        model.addAttribute("ASC", ASC);
        return WKS_TYPOS_TEMPLATE;
    }

    @GetMapping(UPDATE)
    public String getWorkspaceUpdatePage(Model model, @PathVariable String wksName) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        final var wksUpdate = wksOptional
            .map(wksInfo -> new CreateWorkspace(wksInfo.name(), wksInfo.description()))
            .get();
        model.addAttribute("createWorkspace", wksUpdate);
        model.addAttribute("wksName", wksName);
        model.addAttribute("formModified", false);
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);
        return WKS_UPDATE_TEMPLATE;
    }

    //TODO add tests
    @PutMapping(UPDATE)
    public String putWorkspaceUpdate(Model model,
                                     @PathVariable String wksName,
                                     @Valid @ModelAttribute CreateWorkspace wksUpdate,
                                     BindingResult bindingResult) {
        model.addAttribute("wksName", wksName);
        model.addAttribute("formModified", true);
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);

        if (!wksName.equals(wksUpdate.name()) && workspaceService.existsWorkspaceByName(wksUpdate.name())) {
            bindingResult.addError(fieldNameError(wksUpdate));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", wksUpdate);
            return WKS_UPDATE_TEMPLATE;
        }
        try {
            if (workspaceService.updateWorkspace(wksUpdate, wksName).isEmpty()) {
                //TODO send to error page
                log.error("Workspace with name {} not found", wksName);
                return REDIRECT_ROOT;
            }
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(fieldNameError(wksUpdate));
            return WKS_UPDATE_TEMPLATE;
        }
        return REDIRECT_WKS_ROOT + wksUpdate.name();
    }

    @PatchMapping("/token/regenerate")
    public String patchWorkspaceToken(@PathVariable String wksName) {
        if (!workspaceService.existsWorkspaceByName(wksName)) {
            //TODO send to error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        workspaceService.regenerateWorkspaceApiAccessTokenByName(wksName);
        return REDIRECT_WKS_ROOT + wksName + SETTINGS;
    }

    @DeleteMapping
    public String deleteWorkspaceByName(@PathVariable String wksName) {
        if (workspaceService.deleteWorkspaceByName(wksName) == 0) {
            //TODO send to error page
            final var e = new WorkspaceNotFoundException(wksName);
            log.error(e.toString(), e);
        }
        return REDIRECT_ROOT;
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

    @GetMapping(USERS)
    public String getWorkspaceUsersPage(Model model,
                                        @PathVariable String wksName,
                                        @SortDefault(DEFAULT_SORT_FIELD) Pageable pageable) {
        var wksOptional = workspaceService.getWorkspaceInfoByName(wksName);
        if (wksOptional.isEmpty()) {
            //TODO send error page
            log.error("Workspace with name {} not found", wksName);
            return REDIRECT_ROOT;
        }
        model.addAttribute("wksName", wksName);
        model.addAttribute("wksInfo", wksOptional.get());
        getStatisticDataToModel(model, wksName);
        getLastTypoDataToModel(model, wksName);

        var size = Optional.ofNullable(availableSizes.floor(pageable.getPageSize())).orElseGet(availableSizes::first);
        var pageRequest = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        var userPage = accountRepository.findPageAccountByWorkspaceName(pageable, wksName);


        var sort = userPage.getSort()
            .stream()
            .findFirst()
            .orElseGet(() -> asc(DEFAULT_SORT_FIELD));

        model.addAttribute("userPage", userPage);
        model.addAttribute("availableSizes", availableSizes);
        model.addAttribute("sortProp", sort.getProperty());
        model.addAttribute("sortDir", sort.getDirection());
        model.addAttribute("DESC", DESC);
        model.addAttribute("ASC", ASC);
        return WKS_USERS_TEMPLATE;
    }

}

package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.WorkspaceRoleService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.CREATE;
import static io.hexlet.typoreporter.web.Routers.REDIRECT_ROOT;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static io.hexlet.typoreporter.web.Templates.CREATE_WKS;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final WorkspaceService workspaceService;

    private final WorkspaceRoleService workspaceRoleService;

    private final AccountService accountService;

    @GetMapping("/workspaces")
    public String index(final Model model) {
        final var wksInfoList = workspaceService.getAllWorkspacesInfo();
        model.addAttribute("wksInfoList", wksInfoList);
        return "workspaces";
    }

    @GetMapping(CREATE + WORKSPACE)
    public String getCreateWorkspacePage(final Model model) {
        model.addAttribute("createWorkspace", new CreateWorkspace("", "", ""));
        model.addAttribute("formModified", false);
        return CREATE_WKS;
    }

    @PostMapping(CREATE + WORKSPACE)
    public String postCreateWorkspacePage(final Model model,
                                          @Valid @ModelAttribute CreateWorkspace createWorkspace,
                                          BindingResult bindingResult) {
        String currentEmail = accountService.getCurrentAccountEmail();
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", createWorkspace);
            return CREATE_WKS;
        }
        try {
            workspaceService.createWorkspace(createWorkspace);
            workspaceRoleService.addAccountToWorkspaceWhenCreating(createWorkspace.name(), currentEmail);
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("createWorkspace"));
            return CREATE_WKS;
        }
        return REDIRECT_ROOT;
    }
}

package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static io.hexlet.typoreporter.web.Templates.*;
import static io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException.fieldNameError;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final WorkspaceService workspaceService;

    @GetMapping
    public String index(final Model model) {
        final var wksInfoList = workspaceService.getAllWorkspacesInfo();
        model.addAttribute("wksInfoList", wksInfoList);
        return INDEX;
    }

    @GetMapping(CREATE + WORKSPACE)
    public String getCreateWorkspacePage(final Model model) {
        model.addAttribute("createWorkspace", new CreateWorkspace("", ""));
        model.addAttribute("formModified", false);
        return CREATE_WKS;
    }

    @PostMapping(CREATE + WORKSPACE)
    public String postCreateWorkspacePage(final Model model,
                                          @Valid @ModelAttribute CreateWorkspace createWorkspace,
                                          BindingResult bindingResult) {
        model.addAttribute("formModified", true);
        if (workspaceService.existsWorkspaceByName(createWorkspace.name())) {
            bindingResult.addError(fieldNameError(createWorkspace));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", createWorkspace);
            return CREATE_WKS;
        }
        try {
            workspaceService.createWorkspace(createWorkspace);
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(fieldNameError(createWorkspace));
            return CREATE_WKS;
        }
        return REDIRECT_ROOT;
    }
}

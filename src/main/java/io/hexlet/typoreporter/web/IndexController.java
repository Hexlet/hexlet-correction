package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.workspace.CreateWorkspace;
import io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Routers.Workspace.WORKSPACE;
import static io.hexlet.typoreporter.web.exception.WorkspaceAlreadyExistException.fieldNameError;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final WorkspaceService workspaceService;

    @GetMapping
    String index(final Model model) {
        final var wksInfoList = workspaceService.getAllWorkspacesInfo();
        model.addAttribute("wksInfoList", wksInfoList);
        return "index";
    }

    @GetMapping(CREATE + WORKSPACE)
    String getCreateWorkspacePage(final Model model) {
        model.addAttribute("createWorkspace", new CreateWorkspace("", ""));
        model.addAttribute("formModified", false);
        return "create-workspace";
    }

    @PostMapping(CREATE + WORKSPACE)
    String postCreateWorkspacePage(final Model model,
                                   @Valid @ModelAttribute CreateWorkspace createWorkspace,
                                   BindingResult bindingResult) {
        model.addAttribute("formModified", true);
        if (workspaceService.existsWorkspaceByName(createWorkspace.name())) {
            bindingResult.addError(fieldNameError(createWorkspace));
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("createWorkspace", createWorkspace);
            return "create-workspace";
        }
        try {
            workspaceService.createWorkspace(createWorkspace);
        } catch (WorkspaceAlreadyExistException e) {
            bindingResult.addError(fieldNameError(createWorkspace));
            return "create-workspace";
        }
        return REDIRECT_ROOT;
    }
}

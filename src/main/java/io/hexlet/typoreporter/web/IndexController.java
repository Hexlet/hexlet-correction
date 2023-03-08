package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class IndexController {

    private final WorkspaceService workspaceService;

    @GetMapping("/workspaces")
    public String index(final Model model) {
        final var wksInfoList = workspaceService.getAllWorkspacesInfo();
        model.addAttribute("wksInfoList", wksInfoList);
        return "workspaces";
    }
}

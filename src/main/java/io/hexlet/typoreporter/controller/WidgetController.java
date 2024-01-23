package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.WorkspaceService;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WidgetController {

    private final WorkspaceService workspaceService;

    private final TypoService typoService;

    @GetMapping("/typo/form/{wksId}")
    String getWidgetTypoForm(HttpServletResponse response,
                             final Model model,
                             @PathVariable Long wksId) {
        final var wks = workspaceService.getWorkspaceById(wksId);
        if (wks.isEmpty()) {
            log.error("Error during sending widget typo form. Workspace not found");
            return "widget/report-typo-error";
        }
        final var workspace = wks.get();

        response.addHeader("Content-Security-Policy", "frame-ancestors " + workspace.getUrl());
        model.addAttribute("trustedOrigin", workspace.getUrl());

        model.addAttribute("wksId", wksId);
        model.addAttribute("formModified", false);
        model.addAttribute("typoReport", TypoReport.empty());

        log.info("Send widget typo form to '{}'", workspace.getUrl());
        return "widget/typo-form";
    }

    @PostMapping("/typo/form/{wksId}")
    String postWidgetTypoForm(HttpServletResponse response,
                              Model model,
                              @Valid @ModelAttribute TypoReport typoReport,
                              BindingResult bindingResult,
                              @PathVariable Long wksId) {
        final var wks = workspaceService.getWorkspaceById(wksId);

        if (wks.isEmpty()) {
            log.error("Error during saving typo from widget. Workspace not found");
            return "widget/report-typo-error";
        }

        response.addHeader("Content-Security-Policy", "frame-ancestors " + wks.get().getUrl());

        if (bindingResult.hasFieldErrors("reporterComment")) {
            log.warn("Validation error during saving typo from widget. Typo not valid. Errors: {}", bindingResult.getAllErrors());
            model.addAttribute("typoReport", typoReport);
            model.addAttribute("formModified", true);
            return "widget/typo-form";
        }

        if (bindingResult.hasErrors()) {
            log.error("Validation error during saving typo from widget. Typo not valid. Errors: {}", bindingResult.getAllErrors());
            return "widget/report-typo-error";
        }

        try {
            typoService.addTypoReport(typoReport, wksId);
        } catch (Exception e) {
            log.error("Error during saving typo from widget.", e);
            return "widget/report-typo-error";
        }

        return "widget/report-typo-success";
    }
}

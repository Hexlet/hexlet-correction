package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Locale;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.PROFILE_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.PROFILE_SETTINGS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SCRIPT_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SERVICE_HOST;

@Controller
@AllArgsConstructor
public class ProfileController {

    private AccountService accountService;

    @ModelAttribute("module")
    String module() {
        return "profile";
    }

    @GetMapping(path = PROFILE_PATH)
    public String profile(Model model, Principal principal) {
        Account account = accountService.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("account", account);
        return "profile/index";
    }

    @GetMapping(path = PROFILE_SETTINGS_PATH)
    public String settings(Model model, Principal principal, Locale locale) {
        Account account = accountService.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("id", account.getId());
        model.addAttribute("locale", locale.toLanguageTag());
        model.addAttribute("serviceHost", SERVICE_HOST);
        model.addAttribute("scriptPath", SCRIPT_PATH);
        return "profile/settings";
    }
}

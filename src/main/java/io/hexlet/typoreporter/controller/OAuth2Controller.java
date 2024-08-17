package io.hexlet.typoreporter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth")
public class OAuth2Controller {

    @GetMapping("/exception")
    public String handle(final Model model) {
        model.addAttribute("isOAuth2Fail", true);
        return "/login";
    }

    @GetMapping("/exception/name")
    public String handleExceptionName(final Model model) {
        model.addAttribute("isFullNameException", true);
        return "/login";
    }
}

package io.hexlet.typoreporter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/login/oauth")
public class OAuth2Controller {

    @GetMapping("/error/handler")
    public String handleError(HttpServletRequest request, RedirectAttributes attributes) {
        attributes.addFlashAttribute("isGithubFail", true);
        var isSignupPage = Optional.ofNullable(request.getSession().getAttribute("isSignupPage"));
        SecurityContextHolder.getContext().setAuthentication(null);
        if (isSignupPage.isPresent()) {
            return "redirect:/signup";
        }
        return "redirect:/login";
    }
}

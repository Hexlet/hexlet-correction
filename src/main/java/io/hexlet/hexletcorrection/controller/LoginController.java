package io.hexlet.hexletcorrection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.LOGIN_PATH;

@Controller
@RequestMapping(LOGIN_PATH)
public class LoginController {

    @ModelAttribute("module")
    String module() {
        return "login";
    }

    @GetMapping
    public String loginPage() {
        return "login";
    }
}

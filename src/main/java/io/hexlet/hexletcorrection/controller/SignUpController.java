package io.hexlet.hexletcorrection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SIGN_UP_PATH;

@Controller
@RequestMapping(ACCOUNTS_PATH)
public class SignUpController {

    @ModelAttribute("module")
    String module() {
        return "sign-up";
    }

    @GetMapping(path = SIGN_UP_PATH)
    public String signIn() {
        return "sign-up";
    }
}

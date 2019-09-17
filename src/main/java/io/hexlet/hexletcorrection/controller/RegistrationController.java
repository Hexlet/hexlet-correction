package io.hexlet.hexletcorrection.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.REGISTRATION_PATH;

@Controller
@RequestMapping(REGISTRATION_PATH)
public class RegistrationController {

    @ModelAttribute("module")
    String module() {
        return "registration";
    }

    @GetMapping
    public String registrationPage() {
        return "registration";
    }
}

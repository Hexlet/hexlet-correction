package io.hexlet.typoreporter.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import static io.hexlet.typoreporter.web.Templates.*;
import static io.hexlet.typoreporter.web.Routers.LOGIN;

@Controller()
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    @GetMapping(LOGIN)
    public String getLoginPage() {
        return LOGIN_TEMPLATE;
    }
}

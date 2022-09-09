package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.UserDetailServiceImpl;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.REDIRECT_ROOT;
import static io.hexlet.typoreporter.web.Routers.SIGNUP;
import static io.hexlet.typoreporter.web.Routers.LOGIN;
import static io.hexlet.typoreporter.web.Templates.*;

@Controller()
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @GetMapping(LOGIN)
    public String getLoginPage() {
        return LOGIN_TEMPLATE;
    }

    @GetMapping(SIGNUP)
    public String getSignUpPage(final Model model) {
        model.addAttribute("accountForm"); // Account or DTO ???

        return SIGNUP_TEMPLATE;
    }

    @PostMapping(SIGNUP)
    public String createAccount(@ModelAttribute("accountForm") @Valid CreateAccount accountForm,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) {
            return SIGNUP_TEMPLATE;
        }
        // TODO more checks ???

        // if (!accountForm.getPassword().equals(accountForm.getPasswordConfirm())){
        //     model.addAttribute("passwordError", "Passwords doesn't match");
        //     return "registration";
        // }

        if (!userDetailService.saveAccount(accountForm)){
            model.addAttribute("usernameError", "Account already exists");
            return SIGNUP_TEMPLATE;
        }

        return REDIRECT_ROOT;
    }
}

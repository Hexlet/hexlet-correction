package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.AccountDetailService;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import io.hexlet.typoreporter.service.dto.account.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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

    private AccountDetailService accountDetailService;

    @GetMapping(LOGIN)
    public String getLoginPage(final Model model) {
        model.addAttribute("loginAccount", new LoginAccount());
        return LOGIN_TEMPLATE;
    }

    @PostMapping(LOGIN)
    public String login(@ModelAttribute("loginAccount") @Valid LoginAccount loginAccount,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("loginAccount", loginAccount);
            return LOGIN_TEMPLATE;
        }

        UserDetails account = accountDetailService.loadUserByUsername(loginAccount.getUsername());
        if (account == null) {
            return LOGIN_TEMPLATE;
        }

        return REDIRECT_ROOT;
    }

    @GetMapping(SIGNUP)
    public String getSignUpPage(final Model model) {
        model.addAttribute("createAccount", new CreateAccount());
        model.addAttribute("formModified", false);
        return SIGNUP_TEMPLATE;
    }

    @PostMapping(SIGNUP)
    public String createAccount(@ModelAttribute("createAccount") @Valid CreateAccount createAccount,
                                BindingResult bindingResult,
                                Model model) {
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            model.addAttribute("createAccount", createAccount);
            return SIGNUP_TEMPLATE;
        }
        // TODO more checks ???

        // if (!accountForm.getPassword().equals(accountForm.getPasswordConfirm())){
        //     model.addAttribute("passwordError", "Passwords doesn't match");
        //     return "registration";
        // }

        if (!accountDetailService.saveAccount(createAccount)){
            model.addAttribute("usernameError", "Account already exists");
            return SIGNUP_TEMPLATE;
        }

        return REDIRECT_ROOT;
    }
}

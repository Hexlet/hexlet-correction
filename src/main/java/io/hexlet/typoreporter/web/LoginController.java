package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.dto.account.CreateAccount;
import io.hexlet.typoreporter.service.dto.account.LoginAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
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
    private AccountService accountService;

    @GetMapping(LOGIN)
    public String getLoginPage(final Model model) {
        model.addAttribute("loginAccount", new LoginAccount());
        return LOGIN_TEMPLATE;
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

        if (!createAccount.getPassword().equals(createAccount.getConfirmPassword())){
            model.addAttribute("passwordError", "Passwords doesn't match");
            return SIGNUP_TEMPLATE;
        }

        if (!accountService.saveAccount(createAccount)){
            model.addAttribute("usernameError", "Account already exists");
            return SIGNUP_TEMPLATE;
        }

        return REDIRECT_ROOT;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            request.getSession().invalidate();
        }
        return "redirect:/";
    }

}

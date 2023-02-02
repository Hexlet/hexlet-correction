package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.QueryAccount;
import io.hexlet.typoreporter.service.SignUpAccount;
import io.hexlet.typoreporter.service.dto.account.SignupAccount;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static io.hexlet.typoreporter.web.Routers.REDIRECT_ROOT;
import static io.hexlet.typoreporter.web.Routers.SIGNUP;
import static io.hexlet.typoreporter.web.Templates.SIGNUP_TEMPLATE;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class LoginController {

    private final SignUpAccount signUpAccount;

    private final QueryAccount queryAccount;

    @GetMapping(SIGNUP)
    public String getSignUpPage(final Model model) {
        model.addAttribute("signupAccount", new SignupAccount());
        model.addAttribute("formModified", false);
        return SIGNUP_TEMPLATE;
    }

    @PostMapping(SIGNUP)
    public String createAccount(@ModelAttribute("signupAccount") @Valid SignupAccount signupAccount,
                                BindingResult bindingResult,
                                Model model) {
        boolean hasErrors = false;

        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            model.addAttribute("signupAccount", signupAccount);
            hasErrors = true;
        }

        //TODO move into SignUpAccount service in the same transaction
        if (queryAccount.existsByUsername(signupAccount.getUsername())) {
            model.addAttribute("usernameError", "Account with such username already exists");
            hasErrors = true;
        }

        //TODO move into SignUpAccount service in the same transaction
        if (queryAccount.existsByEmail(signupAccount.getEmail())) {
            model.addAttribute("emailError", "Account with such email already exists");
            hasErrors = true;
        }

        if (hasErrors) {
            return SIGNUP_TEMPLATE;
        }

        final var newAccount = signUpAccount.signup(signupAccount);
        if (newAccount == null) {
            return SIGNUP_TEMPLATE;
        }

        final var authenticated = UsernamePasswordAuthenticationToken.authenticated(newAccount.getUsername(), newAccount.getPassword(), List.of(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return REDIRECT_ROOT;
    }
}

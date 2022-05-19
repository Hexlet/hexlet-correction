package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.account.AccountService;
import io.hexlet.typoreporter.service.dto.account.AccountLogin;
import io.hexlet.typoreporter.service.dto.account.AccountRegistration;
import io.hexlet.typoreporter.web.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Templates.LOGIN;
import static io.hexlet.typoreporter.web.Templates.SIGNUP;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final AccountService accountService;

    @PostMapping(SIGNIN_PATH)
    public String auth(final @Valid @ModelAttribute AccountLogin accountLogin,
                       final BindingResult bindingResult,
                       final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountLogin", accountLogin);
            return LOGIN;
        }
        final var account = accountRepository.findAccountByEmail(accountLogin.getEmail())
            .orElseThrow(() -> new AccountNotFoundException(accountLogin.getEmail()));
        if (account == null) {
            return LOGIN;
        }
        return REDIRECT_ROOT;
    }

    @PostMapping(SIGNUP_PATH)
    public String createAccount(final @Valid @ModelAttribute AccountRegistration accountRegistration,
                                final BindingResult bindingResult,
                                final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("accountRegistration", accountRegistration);
            return SIGNUP;
        }
        accountService.registerAccount(accountRegistration);
        return REDIRECT_ROOT;
    }

    @GetMapping(SIGNIN_PATH)
    public String singIn(final Model model) {
        model.addAttribute("accountLogin", new AccountLogin());
        return LOGIN;
    }

    @GetMapping(SIGNUP_PATH)
    public String signUp(final Model model) {
        model.addAttribute("accountRegistration", new AccountRegistration());
        return SIGNUP;
    }
}

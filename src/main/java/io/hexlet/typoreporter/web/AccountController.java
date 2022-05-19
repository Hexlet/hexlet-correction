package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.account.AccountService;
import io.hexlet.typoreporter.service.dto.account.AccountLogin;
import io.hexlet.typoreporter.service.dto.account.AccountRegistration;
import io.hexlet.typoreporter.web.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static io.hexlet.typoreporter.web.Routers.ACCOUNT_PATH;
import static io.hexlet.typoreporter.web.Routers.LOGIN_PATH;
import static io.hexlet.typoreporter.web.Templates.LOGIN;

@Controller
@RequestMapping(path = "")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<String> auth(@RequestBody AccountLogin accountLogin) {
        final var account = accountRepository.findAccountByEmail(accountLogin.getEmail())
            .orElseThrow(() -> new AccountNotFoundException(accountLogin.getEmail()));
        if (account == null) {
            return new ResponseEntity<>("Not authorized. Error", HttpStatus.UNAUTHORIZED);
        } else {
            String body = "Account hello!";
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
    }

    @GetMapping(LOGIN_PATH)
    public String login(final Model model) {
        return LOGIN;
    }

    @PostMapping(ACCOUNT_PATH)
    public Account registrationAccount(@RequestBody final AccountRegistration accountRegistration) {
        return this.accountService.registerAccount(accountRegistration);
    }
}

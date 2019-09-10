package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.controller.exception.AccountNotFoundException;
import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;

@RestController
@RequestMapping(ACCOUNTS_PATH)
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable("id") Long id) {
        return accountService
                .findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountService.create(account);
    }

    @GetMapping
    public List<Account> getAccounts(@RequestParam(required = false) String name) {
        if (name == null) {
            return accountService.findAll();
        }
        return accountService.findByName(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable("id") Long id) {
        accountService.delete(id);
    }
}

package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.AccountPostDto;
import io.hexlet.hexletcorrection.dto.mapper.AccountMapper;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@Controller
@RequestMapping(ACCOUNTS_PATH)
@AllArgsConstructor
public class AccountControllerStatic {

    private static final String TEMPLATE_DIR = "account";
    private static final String VIEW = TEMPLATE_DIR + "/view";
    private static final String LIST = TEMPLATE_DIR + "/list";
    private static final String EDIT = TEMPLATE_DIR + "/edit";
    private static final String NEW = TEMPLATE_DIR + "/new";

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public String getAccounts(Model model, @RequestParam(required = false) String name) {
        List<AccountDto> accountDtos;

        if (isNull(name)) {
            accountDtos = accountService.findAll()
                    .stream()
                    .map(accountMapper::toAccountDto)
                    .collect(Collectors.toList());
            model.addAttribute("accountDtos", accountDtos);
            return LIST;
        } else {
            accountDtos = accountService.findByName(name)
                    .stream()
                    .map(accountMapper::toAccountDto)
                    .collect(Collectors.toList());
            model.addAttribute("accountDtos", accountDtos);
            return LIST;
        }
    }

    @GetMapping("/{id}")
    public String getAccountById(Model model, @PathVariable("id") Long id) {
        Runnable error;
        Consumer<Account> fillModel;

        error = () -> model.addAttribute("error", format("Account with id=%s not found", id));
        fillModel = account -> model.addAttribute("accountDto", accountMapper.toAccountDto(account));

        accountService.findById(id).ifPresentOrElse(fillModel, error);

        return VIEW;
    }

    @GetMapping(value = "/new")
    public String getCreationForm(Model model) {
        model.addAttribute("accountPostDto", new AccountPostDto());
        return NEW;
    }

    @PostMapping(headers = "Content-Type: application/x-www-form-urlencoded")
    public String createAccount(@Valid @RequestBody AccountPostDto accountPostDto) {
        Long savedAccountId = accountMapper.toAccountDto(
                accountService.create(accountMapper.postDtoToAccount(accountPostDto))
        ).getId();

        return "redirect:" + ACCOUNTS_PATH + "/" + savedAccountId;
    }

    @GetMapping(value = "/edit/{id}")
    public String getEditForm(@PathVariable("id") Long id, Model model) {
        Runnable error;
        Consumer<Account> fillModel;

        error = () -> model.addAttribute("error", format("Account with id=%s not found", id));
        fillModel = account -> model.addAttribute("accountDto", accountMapper.toAccountDto(account));

        accountService.findById(id).ifPresentOrElse(fillModel, error);

        return EDIT;
    }

    @PutMapping(headers = "Content-Type: application/x-www-form-urlencoded")
    public String editAccount(@Valid @RequestBody AccountDto accountDto) {
        Long updatedAccountId = accountService
                .create(accountMapper.toAccount(accountDto))
                .getId();

        return "redirect:" + ACCOUNTS_PATH + "/" + updatedAccountId;
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable("id") Long id) {
        accountService.delete(id);
        return "redirect:" + ACCOUNTS_PATH;
    }
}

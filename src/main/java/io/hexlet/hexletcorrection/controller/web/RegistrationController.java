package io.hexlet.hexletcorrection.controller.web;

import io.hexlet.hexletcorrection.controller.validator.AccountPostDtoValidator;
import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.dto.AccountPostDto;
import io.hexlet.hexletcorrection.dto.mapper.AccountMapper;
import io.hexlet.hexletcorrection.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;
import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.REGISTRATION_PATH;

@Controller
@RequestMapping(REGISTRATION_PATH)
@AllArgsConstructor
public class RegistrationController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountPostDtoValidator accountPostDtoValidator;
    private final MessageSource messageSource;

    @ModelAttribute("module")
    String module() {
        return "registration";
    }

    @GetMapping
    public String registration(AccountPostDto accountPostDto) {
        return "registration";
    }

    @PostMapping
    public String registration(@Validated @ModelAttribute("accountPostDto") AccountPostDto accountPostDto,
                               BindingResult bindingResult,
                               Model model,
                               Locale locale) {

        accountPostDtoValidator.validate(accountPostDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        Optional<Account> account = accountService.findByEmail(accountPostDto.getEmail());
        if (account.isPresent()) {
            String errorMessage = messageSource.getMessage("error.registration.user.exist", null, locale);
            model.addAttribute("error", errorMessage);
            return "registration";
        }

        accountService.create(accountMapper.postDtoToAccount(accountPostDto));

        return "redirect:login";
    }
}

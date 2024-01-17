package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.handler.exception.AccountAlreadyExistException;
import io.hexlet.typoreporter.handler.exception.AccountNotFoundException;
import io.hexlet.typoreporter.handler.exception.NewPasswordTheSameException;
import io.hexlet.typoreporter.handler.exception.OldPasswordWrongException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public String getAccountInfoPage(final Model model, final Authentication authentication) {
        final var accountInfo = accountService.getInfoAccount(authentication.getName());
        final var workspaceInfos = accountService.getWorkspacesInfoListByEmail(accountInfo.email());
        model.addAttribute("workspaceRoleInfoList", workspaceInfos);
        model.addAttribute("accInfo", accountInfo);
        return "account/acc-info";
    }

    @GetMapping("/update")
    public String getProfilePage(final Model model,
                                 final Authentication authentication) {
        final String email = authentication.getName();
        final var updateProfile = accountService.getUpdateProfile(email);
        model.addAttribute("formModified", false);
        model.addAttribute("updateProfile", updateProfile);
        return "account/prof-update";
    }

    @PutMapping("/update")
    public String putProfileUpdate(final Model model,
                                   final @Valid @ModelAttribute("updateProfile") UpdateProfile updateProfile,
                                   final BindingResult bindingResult,
                                   final Authentication authentication) {
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            return "account/prof-update";
        }
        try {
            final String email = authentication.getName();
            Account updatedAccount = accountService.updateProfile(updateProfile, email);
            final var authenticated = UsernamePasswordAuthenticationToken.authenticated(updatedAccount.getEmail(),
                updatedAccount.getPassword(), List.of(() -> "ROLE_USER"));
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return "redirect:/account";
        } catch (AccountAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("updateProfile"));
            return "account/prof-update";
        }
    }

    @GetMapping("/password")
    public String getPasswordPage(final Model model) {
        model.addAttribute("formModified", false);
        model.addAttribute("updatePassword", new UpdatePassword());
        return "account/pass-update";
    }

    @PutMapping("/password")
    public String putPasswordUpdate(final Model model,
                                    final @Valid @ModelAttribute("updatePassword") UpdatePassword updatePassword,
                                    final BindingResult bindingResult,
                                    final Authentication authentication) {
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            return "account/pass-update";
        }
        try {
            final String email = authentication.getName();
            Account updatedAccount = accountService.updatePassword(updatePassword, email);
            final var authenticated = UsernamePasswordAuthenticationToken.authenticated(updatedAccount.getEmail(),
                updatedAccount.getPassword(), List.of(() -> "ROLE_USER"));
            SecurityContextHolder.getContext().setAuthentication(authenticated);
            return "redirect:/account";
        } catch (OldPasswordWrongException | NewPasswordTheSameException e) {
            bindingResult.addError(e.toFieldError("updatePassword"));
            return "account/pass-update";
        }
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public String accountNotFoundException(AccountNotFoundException e) {
        log.error("Account not found", e);
        return "/error-general";
    }

}

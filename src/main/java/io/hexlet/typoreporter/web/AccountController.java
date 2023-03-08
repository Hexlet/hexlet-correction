package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.web.exception.AccountAlreadyExistException;
import io.hexlet.typoreporter.web.exception.NewPasswordTheSameException;
import io.hexlet.typoreporter.web.exception.OldPasswordWrongException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public String getAccountInfoPage(final Model model, final Authentication authentication) {
        final var maybeAccInfo = accountService.getInfoAccount(authentication.getName());
        if (maybeAccInfo.isEmpty()) {
            log.error("Error during getting account info page. Account info not found");
            return "/error-general";
        }
        final var accInfo = maybeAccInfo.get();
        final var workspaceInfoList =  accountService.getWorkspacesInfoListByUsername(accInfo.username());

        model.addAttribute("workspaceRoleInfoList", workspaceInfoList);
        model.addAttribute("accInfo", accInfo);

        return "account/acc-info";
    }

    @GetMapping("/profile")
    public String getProfilePage(final Model model,
                                 final Authentication authentication) {
        final String name = authentication.getName();
        final var updateProfile = accountService.getUpdateProfile(name);

        if (updateProfile.isEmpty()) {
            log.error("Error during getting profile for update. UpdateProfile not found");
            return "/error-general";
        }

        model.addAttribute("formModified", false);
        model.addAttribute("updateProfile", updateProfile.get());

        return "account/prof-update";
    }

    @PutMapping("/profile/update")
    public String putProfileUpdate(final Model model,
                                   final @Valid @ModelAttribute("updateProfile") UpdateProfile updateProfile,
                                   final BindingResult bindingResult,
                                   final Authentication authentication) {

        model.addAttribute("formModified", true);

        if (bindingResult.hasErrors()) {
            return "account/prof-update";
        }

        Optional<Account> updatedAccount;
        try {
            final String name = authentication.getName();
            updatedAccount = accountService.updateProfile(updateProfile, name);

            if (updatedAccount.isEmpty()) {
                log.error("Error during getting updated account. Updated account not found");
                return "/error-general";
            }
        } catch (AccountAlreadyExistException e) {
            bindingResult.addError(e.toFieldError("updateProfile"));
            return "account/prof-update";
        }

        final var authenticated = UsernamePasswordAuthenticationToken.authenticated(updatedAccount.get().getUsername(),
            updatedAccount.get().getPassword(), List.of(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return "redirect:/account/";
    }

    @GetMapping("/password")
    public String getPasswordPage(final Model model) {
        model.addAttribute("formModified", false);
        model.addAttribute("updatePassword", new UpdatePassword());

        return "account/pass-update";
    }

    @PutMapping("/password/update")
    public String putPasswordUpdate(final Model model,
                                   final @Valid @ModelAttribute("updatePassword") UpdatePassword updatePassword,
                                   final BindingResult bindingResult,
                                    final Authentication authentication) {

        model.addAttribute("formModified", true);

        if (bindingResult.hasErrors()) {
            return "account/pass-update";
        }

        Optional<Account> updatedAccount;
        try {
            final String name = authentication.getName();
            updatedAccount = accountService.updatePassword(updatePassword, name);

            if (updatedAccount.isEmpty()) {
                log.error("Error during getting updated account with new password. Updated account not found");
                return "/error-general";
            }
        } catch (OldPasswordWrongException | NewPasswordTheSameException e) {
            bindingResult.addError(e.toFieldError("updatePassword"));
            return "account/pass-update";
        }

        final var authenticated = UsernamePasswordAuthenticationToken.authenticated(updatedAccount.get().getUsername(),
            updatedAccount.get().getPassword(), List.of(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return "redirect:/account/";
    }
}

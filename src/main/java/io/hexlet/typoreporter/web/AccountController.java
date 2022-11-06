package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.QueryAccount;
import io.hexlet.typoreporter.service.dto.account.UpdatePassword;
import io.hexlet.typoreporter.service.dto.account.UpdateProfile;
import io.hexlet.typoreporter.web.exception.EmailAlreadyExistException;
import io.hexlet.typoreporter.web.exception.OldPasswordWrongException;
import io.hexlet.typoreporter.web.exception.UsernameAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.AbstractThrowableProblem;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import static io.hexlet.typoreporter.web.Routers.*;
import static io.hexlet.typoreporter.web.Routers.Account.*;
import static io.hexlet.typoreporter.web.Templates.*;

@Slf4j
@Controller
@RequestMapping(ACCOUNT)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final QueryAccount queryAccount;

    private final PasswordEncoder passwordEncoder;

    private final Authentication authentication;

    @GetMapping
    public String getAccountInfoPage(final Model model) {
        final String name = authentication.getName();
        final var accountInfo = accountService.getInfoAccount(name);
        if (accountInfo.isEmpty()) {
            //TODO send to error page
            log.error("Account not found");
            return REDIRECT_ROOT;
        }

        model.addAttribute("accInfo", accountInfo.get());

        return ACC_INFO_TEMPLATE;
    }

    @GetMapping(PROFILE)
    public String getProfilePage(final Model model) {
        final String name = authentication.getName();
        final var updateProfile = accountService.getUpdateProfile(name);
        if (updateProfile.isEmpty()) {
            //TODO send to error page
            log.error("Account not found");
            return REDIRECT_ROOT;
        }

        model.addAttribute("formModified", false);
        model.addAttribute("updateProfile", updateProfile.get());

        return PROF_UPDATE_TEMPLATE;
    }

    @PutMapping(PROFILE + UPDATE)
    public String putProfileUpdate(final Model model,
                                   final @Valid @ModelAttribute("updateProfile") UpdateProfile updateProfile,
                                   final BindingResult bindingResult) {

        model.addAttribute("formModified", true);

        final String name = authentication.getName();
        final var sourceAccount = accountService.getAccount(name);
        if (sourceAccount.isEmpty()) {
            //TODO send to error page
            log.error("Account not found");
            return REDIRECT_ROOT;
        }

        final String username = sourceAccount.get().getUsername();
        if (!username.equals(updateProfile.getUsername()) && queryAccount.existsByUsername(updateProfile.getUsername())) {
            bindingResult.addError(UsernameAlreadyExistException.fieldNameError(updateProfile));
        }
        final String email = sourceAccount.get().getEmail();
        if (!email.equals(updateProfile.getEmail()) && queryAccount.existsByEmail(updateProfile.getEmail())) {
            bindingResult.addError(EmailAlreadyExistException.fieldNameError(updateProfile));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("updateProfile", updateProfile);
            return PROF_UPDATE_TEMPLATE;
        }

        Optional<Account> updatedAccount;
        try {
            updatedAccount = accountService.updateProfile(updateProfile, name);
            if (updatedAccount.isEmpty()) {
                //TODO send to error page
                log.error("Account not found");
                return REDIRECT_ROOT;
            }
            // never happens
        } catch (AbstractThrowableProblem e) {
            log.error("Failed to update account profile");
            return PROF_UPDATE_TEMPLATE;
        }

        final var authenticated = UsernamePasswordAuthenticationToken.authenticated(updatedAccount.get().getUsername(),
            updatedAccount.get().getPassword(), List.of(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return REDIRECT_ACC_ROOT;
    }

    @GetMapping(PASSWORD)
    public String getPasswordPage(final Model model) {
        model.addAttribute("formModified", false);
        model.addAttribute("updatePassword", new UpdatePassword());

        return PASS_UPDATE_TEMPLATE;
    }


    @PutMapping(PASSWORD + UPDATE)
    public String putPasswordUpdate(final Model model,
                                   final @Valid @ModelAttribute("updatePassword") UpdatePassword updatePassword,
                                   final BindingResult bindingResult) {


        model.addAttribute("formModified", true);

        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(updatePassword.getOldPassword(), password)) {
            bindingResult.addError(OldPasswordWrongException.fieldNameError());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("updatePassword", updatePassword);
            return PASS_UPDATE_TEMPLATE;
        }

        Optional<Account> updatedAccount;
        try {
            updatedAccount = accountService.updatePassword(updatePassword, name);
            if (updatedAccount.isEmpty()) {
                //TODO send to error page
                log.error("Account not found");
                return REDIRECT_ROOT;
            }
            // never happens
        } catch (AbstractThrowableProblem e) {
            log.error("Failed to update account password");
            return PASS_UPDATE_TEMPLATE;
        }

        final var authenticated = UsernamePasswordAuthenticationToken.authenticated(updatedAccount.get().getUsername(),
            updatedAccount.get().getPassword(), List.of(() -> "ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        return REDIRECT_ACC_ROOT;
    }
}

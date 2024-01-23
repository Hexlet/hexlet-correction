package io.hexlet.typoreporter.controller;

import io.hexlet.typoreporter.domain.workspace.AccountRole;
import io.hexlet.typoreporter.service.account.EmailAlreadyExistException;
import io.hexlet.typoreporter.service.account.UsernameAlreadyExistException;
import io.hexlet.typoreporter.service.account.signup.SignupAccountMapper;
import io.hexlet.typoreporter.service.account.signup.SignupAccountUseCase;
import io.hexlet.typoreporter.service.dto.account.InfoAccount;
import io.hexlet.typoreporter.web.model.SignupAccountModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class SignupController {

    private final SignupAccountUseCase signupAccountUseCase;
    private final SignupAccountMapper signupAccountMapper;
    private final SecurityContextRepository securityContextRepository;

    @GetMapping("/signup")
    public String getSignUpPage(final Model model) {
        model.addAttribute("signupAccount", new SignupAccountModel());
        model.addAttribute("formModified", false);
        return "account/signup";
    }

    @PostMapping("/signup")
    public String createAccount(@ModelAttribute("signupAccount") @Valid SignupAccountModel signupAccountModel,
                                BindingResult bindingResult,
                                Model model,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        model.addAttribute("formModified", true);
        if (bindingResult.hasErrors()) {
            return "account/signup";
        }

        final InfoAccount newAccount;
        try {
            newAccount = signupAccountUseCase.signup(signupAccountMapper.toSignupAccount(signupAccountModel));
            final var authentication = UsernamePasswordAuthenticationToken.
                authenticated(newAccount.email(), null, List.of(AccountRole.ROLE_GUEST::name));
            autoLoginAfterSignup(request, response, authentication);
            return "redirect:/workspaces";
        } catch (UsernameAlreadyExistException e) {
            log.warn(e.getMessage(), e);
            bindingResult.addError(new FieldError("signupAccount", "username", e.getUsername(), false, null, null, e.getMessage()));
            return "account/signup";
        } catch (EmailAlreadyExistException e) {
            log.warn(e.getMessage(), e);
            bindingResult.addError(new FieldError("signupAccount", "email", e.getEmail(), false, null, null, e.getMessage()));
            return "account/signup";
        }
    }

    private void autoLoginAfterSignup(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}

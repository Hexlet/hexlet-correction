package io.hexlet.typoreporter.handler;

import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.domain.account.OAuth2GithubUser;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.repository.AccountSocialLinkRepository;
import io.hexlet.typoreporter.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSocialLinkRepository accountSocialLinkRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("github".equals(authenticationToken.getAuthorizedClientRegistrationId())) {
            OAuth2GithubUser user = (OAuth2GithubUser) authentication.getPrincipal();
            var accountLink = accountSocialLinkRepository.findAccountSocialLinkByLoginAndAuthProvider(user.getId(), AuthProvider.GITHUB);
            if (accountLink.isEmpty()) {
                accountService.createGithubUser(user);
            } else {
                accountService.updateGithubUser(accountLink.get().getAccount(), user);
            }
        }
        response.sendRedirect("/workspaces");
    }
}

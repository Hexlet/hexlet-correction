package io.hexlet.typoreporter.handler;

import io.hexlet.typoreporter.domain.account.CustomOAuth2User;
import io.hexlet.typoreporter.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private AccountService accountService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
        accountService.createOrUpdate(user);
        request.getSession().setAttribute("isOAuth2Fail", false);
        response.sendRedirect("/workspaces");
    }
}

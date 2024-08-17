package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.domain.account.CustomOAuth2User;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AccountService accountService;
    private final GithubService githubService;

    public OAuth2User loadUser(OAuth2UserRequest userRequest)  {
        OAuth2User user = super.loadUser(userRequest);
        var email = githubService.getPrivateEmail(userRequest.getAccessToken().getTokenValue());
        var customUser = new CustomOAuth2User(user, email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            customUser, customUser.getPassword(), customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        accountService.processOAuthPostLogin(customUser);

        return customUser;
    }
}

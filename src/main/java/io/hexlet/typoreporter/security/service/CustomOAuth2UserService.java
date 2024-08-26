package io.hexlet.typoreporter.security.service;

import io.hexlet.typoreporter.domain.account.CustomOAuth2User;
import io.hexlet.typoreporter.service.GithubService;
import io.hexlet.typoreporter.service.dto.oauth2.PrivateEmail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final GithubService githubService;

    public OAuth2User loadUser(OAuth2UserRequest userRequest)  {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        PrivateEmail privateEmail = githubService.getPrivateEmail(userRequest.getAccessToken().getTokenValue());
        if (privateEmail.getEmail() == null || privateEmail.getEmail().isEmpty()) {
            return null;
        }
        return new CustomOAuth2User(oAuth2User, privateEmail);
    }
}

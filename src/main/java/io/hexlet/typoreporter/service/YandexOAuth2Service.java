package io.hexlet.typoreporter.service;

import io.hexlet.typoreporter.handler.exception.DuplicateYandexIdException;
import io.hexlet.typoreporter.service.dto.oauth.CustomOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class YandexOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AccountService accountService;

    public YandexOAuth2Service(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("default_email");
        String login = (String) attributes.get("login");
        String firstName = (String) attributes.get("first_name");
        String lastName = (String) attributes.get("last_name");
        String yandexId = (String) attributes.get("id");

        try {
            accountService.processYandexOAuth2User(email, login, firstName, lastName, yandexId);

            Map<String, Object> securityAttributes = new HashMap<>(attributes);
            securityAttributes.put("email", email);

            return new CustomOAuth2User(Collections.singleton(new SimpleGrantedAuthority("USER")),
            securityAttributes,
            "email");
        } catch (DuplicateYandexIdException e) {
            throw new OAuth2AuthenticationException("Этот яндекс аккаунт уже привязан к другому email");
        }
    }
}

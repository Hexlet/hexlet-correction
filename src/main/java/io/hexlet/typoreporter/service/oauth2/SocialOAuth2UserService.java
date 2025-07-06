package io.hexlet.typoreporter.service.oauth2;

import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import io.hexlet.typoreporter.service.dto.oauth2.CustomOAuth2User;
import io.hexlet.typoreporter.service.dto.oauth2.OAuth2UserInfo;
import io.hexlet.typoreporter.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SocialOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultUserService;
    private final OAuth2UserInfoMapperService userInfoMapperService;
    private final AccountService accountService;
    private final WebClient githubWebClient;

    public static final String NAME_ATTRIBUTE_KEY = "email";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        AuthProvider authProvider = resolveProvider(userRequest);
        OAuth2User oAuth2User = defaultUserService.loadUser(userRequest);
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        OAuth2UserInfo userInfo = userInfoMapperService.getUserInfo(attributes, authProvider);
        String accessTokenValue = userRequest.getAccessToken().getTokenValue();
        String email = resolveEmail(userInfo, accessTokenValue, authProvider);
        createNewAccountIfNotExists(userInfo, email, accessTokenValue, authProvider);

        attributes.put(NAME_ATTRIBUTE_KEY, email);

        return new CustomOAuth2User(
            oAuth2User.getAuthorities(),
            attributes,
            NAME_ATTRIBUTE_KEY,
            userInfo.getNickname()
        );
    }

    private AuthProvider resolveProvider(OAuth2UserRequest userRequest) {
        String oAuth2Provider = userRequest.getClientRegistration().getRegistrationId();
        AuthProvider authProvider = AuthProvider.fromString(oAuth2Provider);
        if (authProvider == null) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error("unsupported_provider", "Unsupported provider: " + oAuth2Provider, null));
        }
        return authProvider;
    }

    private String resolveEmail(OAuth2UserInfo userInfo, String accessTokenValue,
                                AuthProvider authProvider) {
        String email = userInfo.getEmail();
        if (email == null && authProvider == AuthProvider.GITHUB) {
            email = getEmailFromGithub(accessTokenValue);
        }
        if (email == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("email_not_received",
                "Email from provider " + authProvider.name() + " not received", null));
        }
        return TextUtils.toLowerCaseData(email);
    }

    private String getEmailFromGithub(String accessTokenValue) {
        try {
            List<Map<String, Object>> emails = githubWebClient.get()
                .uri("/emails")
                .headers(headers -> headers.setBearerAuth(accessTokenValue))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() { })
                .block();

            if (emails == null) {
                return null;
            }

            return emails.stream()
                .filter(e -> Boolean.TRUE.equals(e.get("primary")))
                .map(e -> (String) e.get("email"))
                .findFirst()
                .orElse(null);
        } catch (WebClientResponseException e) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error("failed_retrieve_email", "Failed to retrieve email from GitHub", null));
        }
    }

    @Transactional
    private void createNewAccountIfNotExists(OAuth2UserInfo userInfo, String email,
                                             String accountTokenValue, AuthProvider authProvider) {
        if (!accountService.existsByEmail(email)) {
            var newAccount = new SignupAccount(
                userInfo.getNickname(),
                email,
                accountTokenValue,
                userInfo.getFirstName(),
                userInfo.getLastName(),
                authProvider.name()
            );
            accountService.signup(newAccount);
        }
    }
}

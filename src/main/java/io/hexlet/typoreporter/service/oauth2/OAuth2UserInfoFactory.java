package io.hexlet.typoreporter.service.oauth2;

import io.hexlet.typoreporter.domain.account.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, String accessToken,
                                                   Map<String, Object> attributes) {
        return switch (AuthProvider.fromName(provider.toUpperCase())) {
            case GITHUB -> new GithubOAuth2UserInfo(accessToken, attributes);
            case YANDEX -> new YandexOAuth2UserInfo(accessToken, attributes);
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };
    }
}

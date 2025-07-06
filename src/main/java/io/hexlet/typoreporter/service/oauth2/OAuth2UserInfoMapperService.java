package io.hexlet.typoreporter.service.oauth2;

import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.service.dto.oauth2.OAuth2UserInfo;
import io.hexlet.typoreporter.service.mapper.oauth2.GithubOAuth2UserInfoMapper;
import io.hexlet.typoreporter.service.mapper.oauth2.YandexOAuth2UserInfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserInfoMapperService {

    private final GithubOAuth2UserInfoMapper githubUserInfoMapper;
    private final YandexOAuth2UserInfoMapper yandexUserInfoMapper;

    public OAuth2UserInfo getUserInfo(Map<String, Object> attributes, AuthProvider authProvider) {
        return switch (authProvider) {
            case GITHUB -> githubUserInfoMapper.toUserInfo(attributes);
            case YANDEX -> yandexUserInfoMapper.toUserInfo(attributes);
            default -> throw new IllegalArgumentException("Unsupported OAuth2 provider: " + authProvider.name());
        };
    }
}

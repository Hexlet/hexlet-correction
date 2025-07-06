package io.hexlet.typoreporter.service.oauth2;

import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.service.dto.oauth2.OAuth2UserInfo;
import io.hexlet.typoreporter.service.mapper.oauth2.GithubOAuth2UserInfoMapper;
import io.hexlet.typoreporter.service.mapper.oauth2.YandexOAuth2UserInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2UserInfoMapperServiceTest {

    @Mock
    private GithubOAuth2UserInfoMapper githubUserInfoMapper;

    @Mock
    private YandexOAuth2UserInfoMapper yandexUserInfoMapper;

    @Mock
    private OAuth2UserInfo userInfo;

    @InjectMocks
    private OAuth2UserInfoMapperService mapperService;

    @Test
    void testGetUserInfoWhenGithubProvider() {
        Map<String, Object> attributes = Collections.emptyMap();
        when(githubUserInfoMapper.toUserInfo(attributes)).thenReturn(userInfo);

        assertThat(mapperService.getUserInfo(attributes, AuthProvider.GITHUB))
            .isInstanceOf(OAuth2UserInfo.class);
    }

    @Test
    void testGetUserInfoWhenYandexProvider() {
        Map<String, Object> attributes = Collections.emptyMap();
        when(yandexUserInfoMapper.toUserInfo(attributes)).thenReturn(userInfo);

        assertThat(mapperService.getUserInfo(attributes, AuthProvider.YANDEX))
            .isInstanceOf(OAuth2UserInfo.class);
    }

    @Test
    void testGetUserInfoWhenUnsupportedOAuth2Provider() {
        Map<String, Object> attributes = Collections.emptyMap();

        assertThatThrownBy(() -> mapperService.getUserInfo(attributes, AuthProvider.EMAIL))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Unsupported OAuth2 provider: EMAIL");
    }
}

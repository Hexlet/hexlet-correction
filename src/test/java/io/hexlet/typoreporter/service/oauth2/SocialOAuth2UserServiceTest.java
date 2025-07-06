package io.hexlet.typoreporter.service.oauth2;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import io.hexlet.typoreporter.service.dto.oauth2.CustomOAuth2User;
import io.hexlet.typoreporter.service.dto.oauth2.OAuth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WireMockTest
class SocialOAuth2UserServiceTest {

    @Mock
    private DefaultOAuth2UserService defaultUserService;

    @Mock
    private OAuth2User user;

    @Mock
    private OAuth2UserInfoMapperService userInfoMapperService;

    @Mock
    private OAuth2UserInfo userInfo;

    @Mock
    private AccountService accountService;

    private SocialOAuth2UserService userService;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @BeforeEach
    void setup() {
        WebClient githubWebClient = WebClient.builder()
            .baseUrl("http://localhost:" + wireMockExtension.getPort())
            .build();

        userService = new SocialOAuth2UserService(
            defaultUserService,
            userInfoMapperService,
            accountService,
            githubWebClient
        );
    }

    @Test
    void testResolveProviderWhenUnsupportedProvider() {
        var request = mockRequest("unknown", "token");

        assertThatThrownBy(() -> userService.loadUser(request))
            .isInstanceOf(OAuth2AuthenticationException.class)
            .hasMessageContaining("Unsupported provider: unknown");
    }

    @Test
    void testResolveEmailWhenEmailNotRecievedFromAnyProvider() {
        var request = mockRequest("Github", "token");

        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/emails"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                      {"email": "test@example.com", "primary": false}
                    ]
                    """)));

        when(defaultUserService.loadUser(request)).thenReturn(user);
        when(user.getAttributes()).thenReturn(Collections.emptyMap());
        when(userInfoMapperService.getUserInfo(Collections.emptyMap(), AuthProvider.GITHUB)).thenReturn(userInfo);
        when(userInfo.getEmail()).thenReturn(null);

        assertThatThrownBy(() -> userService.loadUser(request))
            .isInstanceOf(OAuth2AuthenticationException.class)
            .hasMessageContaining("Email from provider GITHUB not received");
    }

    @Test
    void testResolveEmailWhenEmailFailedRetrieveFromGithubProvider() {
        var request = mockRequest("Github", "token");

        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/emails"))
            .willReturn(aResponse().withStatus(500)));

        when(defaultUserService.loadUser(request)).thenReturn(user);
        when(user.getAttributes()).thenReturn(Collections.emptyMap());
        when(userInfoMapperService.getUserInfo(Collections.emptyMap(), AuthProvider.GITHUB)).thenReturn(userInfo);
        when(userInfo.getEmail()).thenReturn(null);

        assertThatThrownBy(() -> userService.loadUser(request))
            .isInstanceOf(OAuth2AuthenticationException.class)
            .hasMessageContaining("Failed to retrieve email from GitHub");
    }

    @Test
    void testLoadUserWhenUserExists() {
        var request = mockRequest("Github", "token");

        when(defaultUserService.loadUser(request)).thenReturn(user);
        when(user.getAttributes()).thenReturn(Collections.emptyMap());
        when(user.getAuthorities()).thenReturn(Collections.emptyList());

        OAuth2UserInfo ui = new OAuth2UserInfo();
        ui.setEmail("TesT@example.com");
        ui.setNickname("johndoe");
        ui.setFirstName("John");
        ui.setLastName("Doe");

        when(userInfoMapperService.getUserInfo(Collections.emptyMap(), AuthProvider.GITHUB)).thenReturn(ui);
        when(accountService.existsByEmail("test@example.com")).thenReturn(true);

        OAuth2User oAuth2User = userService.loadUser(request);

        assertThat(oAuth2User).isInstanceOf(CustomOAuth2User.class);
        verify(accountService).existsByEmail("test@example.com");
        verify(accountService, never()).signup(any());
    }

    @Test
    void testLoadUserWhenUserNotExists() {
        var request = mockRequest("Github", "token");

        when(defaultUserService.loadUser(request)).thenReturn(user);
        when(user.getAttributes()).thenReturn(Collections.emptyMap());
        when(user.getAuthorities()).thenReturn(Collections.emptyList());

        OAuth2UserInfo ui = new OAuth2UserInfo();
        ui.setEmail("New-Test@example.com");
        ui.setNickname("johndoe");
        ui.setFirstName("John");
        ui.setLastName("Doe");

        when(userInfoMapperService.getUserInfo(Collections.emptyMap(), AuthProvider.GITHUB)).thenReturn(ui);
        when(accountService.existsByEmail("new-test@example.com")).thenReturn(false);

        OAuth2User user = userService.loadUser(request);

        assertThat(user.getAttributes()).containsEntry("email", "new-test@example.com");
        ArgumentCaptor<SignupAccount> captor = ArgumentCaptor.forClass(SignupAccount.class);
        verify(accountService).signup(captor.capture());
        SignupAccount signup = captor.getValue();
        assertThat(signup.email()).isEqualTo("new-test@example.com");

        CustomOAuth2User customUser = (CustomOAuth2User) user;

        assertThat(customUser.getName()).isEqualTo("new-test@example.com");
        assertThat(customUser.getNickname()).isEqualTo("johndoe");
        assertThat(customUser.getAuthorities()).isEmpty();
        assertThat(customUser.getAttributes()).containsEntry("email", "new-test@example.com");
    }

    public static OAuth2UserRequest mockRequest(String registrationId, String tokenValue) {
        return new OAuth2UserRequest(
            mockRegistration(registrationId),
            new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                Instant.now().minusSeconds(60),
                Instant.now().plusSeconds(3600)
            )
        );
    }

    public static ClientRegistration mockRegistration(String registrationId) {
        return ClientRegistration.withRegistrationId(registrationId)
            .authorizationUri("https://example.com/oauth2/authorize")
            .tokenUri("https://example.com/oauth2/token")
            .clientId("client-id")
            .clientSecret("client-secret")
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            .scope("read:user", "user:email")
            .authorizationGrantType(org.springframework.security.oauth2.core.AuthorizationGrantType.AUTHORIZATION_CODE)
            .userInfoUri("https://example.com/user")
            .userNameAttributeName("id")
            .build();
    }
}

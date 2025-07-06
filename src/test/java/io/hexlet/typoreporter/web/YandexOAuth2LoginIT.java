package io.hexlet.typoreporter.web;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REGISTRATION_ID;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.hexlet.typoreporter.domain.account.AuthProvider;
import io.hexlet.typoreporter.domain.account.Account;
import io.hexlet.typoreporter.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class YandexOAuth2LoginIT {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    static WireMockServer wireMockServer;

    @DynamicPropertySource
    static void property(DynamicPropertyRegistry registry) {
        if (wireMockServer == null) {
            wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
            wireMockServer.start();
        }
        String base = "http://localhost:" + wireMockServer.port();

        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        registry.add("spring.security.oauth2.client.registration.yandex.client-id",
            () -> "test-client-id");
        registry.add("spring.security.oauth2.client.registration.yandex.client-secret",
            () -> "test-client-secret");
        registry.add("spring.security.oauth2.client.registration.yandex.provider",
            () -> "yandex");
        registry.add("spring.security.oauth2.client.registration.yandex.redirect-uri",
            () -> "{baseUrl}/login/oauth2/code/{registrationId}");
        registry.add("spring.security.oauth2.client.registration.yandex.authorization-grant-type",
            () -> "authorization_code");
        registry.add("spring.security.oauth2.client.registration.yandex.scope",
            () -> "email");
        registry.add("spring.security.oauth2.client.registration.yandex.user-name-attribute",
            () -> "default_email");
        registry.add("spring.security.oauth2.client.provider.yandex.authorization-uri",
            () -> base + "/oauth/authorize");
        registry.add("spring.security.oauth2.client.provider.yandex.token-uri",
            () -> base + "/oauth/token");
        registry.add("spring.security.oauth2.client.provider.yandex.user-info-uri",
            () -> base + "/userinfo");
    }

    @BeforeEach
    void setup() {
        wireMockServer.resetAll();

        wireMockServer.stubFor(post("/oauth/token")
            .willReturn(okJson("{ \"access_token\":\"mock-token\",\"token_type\":\"bearer\" }")));

        wireMockServer.stubFor(WireMock.get(urlEqualTo("/userinfo"))
            .withHeader("Authorization", equalTo("Bearer mock-token"))
            .willReturn(okJson("""
          {
            "id":"yandex-id",
            "default_email":"test@yandex.ru",
            "login":"yandex_user"
          }
        """)));
    }

    @Test
    void yandexLogin() throws Exception {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(REGISTRATION_ID, "yandex");

        OAuth2AuthorizationRequest authRequest = OAuth2AuthorizationRequest
            .authorizationCode()
            .clientId("test-client-id")
            .authorizationUri("http://localhost:" + wireMockServer.port() + "/oauth/authorize")
            .redirectUri("http://localhost/login/oauth2/code/yandex")
            .state("myspecialstate")
            .attributes(attributes)
            .build();

        String key = HttpSessionOAuth2AuthorizationRequestRepository
            .class.getName() + ".AUTHORIZATION_REQUEST";
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(key, authRequest);

        mockMvc.perform(get("/login/oauth2/code/yandex")
                .session(session)
                .param("code", "fake-code")
                .param("state", "myspecialstate")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/workspaces"));

        Account account = accountRepository
            .findAccountByEmail("test@yandex.ru")
            .orElseThrow();
        assertThat(account.getAuthProvider()).isEqualTo(AuthProvider.YANDEX);
        assertThat(account.getUsername()).isEqualTo("yandex_user");
    }

    @Test
    void yandexBadLogin() throws Exception {
        wireMockServer.stubFor(post("/oauth/token").willReturn(aResponse().withStatus(400)));

        mockMvc.perform(get("/login/oauth2/code/yandex")
                .param("code", "bad")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));

        assertThat(accountRepository.count()).isZero();
    }
}

package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.account.signup.SignupAccount;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WireMockTest
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
public class GithubOAuth2LoginIT {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AccountService accountService;

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;

    private final String code = "fake-auth-code";
    private final String accessToken = "fake-access-token";

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");


    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);

        String wireMockPort = String.valueOf(wireMockExtension.getPort());
        registry.add("spring.security.oauth2.client.provider.github.authorization-uri",
            () -> "http://localhost:" + wireMockPort + "/login/oauth/authorize");
        registry.add("spring.security.oauth2.client.provider.github.token-uri",
            () -> "http://localhost:" + wireMockPort + "/login/oauth/access_token");
        registry.add("spring.security.oauth2.client.provider.github.user-info-uri",
            () -> "http://localhost:" + wireMockPort + "/user");
    }

    @BeforeEach
    void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .defaultRequest(get("/").header("Host", "localhost:" + port))
            .apply(springSecurity())
            .build();
    }

    @Test
    void testGithubOAuth2AuthenticationNewUser() throws Exception {

        // Securing GET /oauth2/authorization/github
        var result = mockMvc.perform(get("/oauth2/authorization/github"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", startsWith(wireMockExtension.baseUrl() + "/login/oauth/authorize")))
            .andExpect(header().string("Location", containsString("client_id=fake-client-id")))
            .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);

        String encodedState = fromUriString(location).build().getQueryParams().getFirst("state");
        assertNotNull(encodedState);
        String state = URLDecoder.decode(encodedState, StandardCharsets.UTF_8);

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session);

        // HTTP POST https://github.com/login/oauth/access_token
        wireMockExtension.stubFor(WireMock.post(urlPathEqualTo("/login/oauth/access_token"))
            .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "access_token": "fake-access-token",
                        "token_type": "bearer"
                    }
                    """)));

        // HTTP GET https://api.github.com/user
        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/user"))
            .withHeader("Authorization", equalTo("Bearer " + accessToken))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "login": "johndoe",
                        "name": "John Doe",
                        "email": null
                    }
                    """)));

        // HTTP GET https://api.github.com/user/emails
        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/user/emails"))
            .withHeader("Authorization", equalTo("Bearer " + accessToken))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                        {
                            "email": "JohnDoe@example.com",
                            "primary": true
                        }
                    ]
                    """)));

        assertFalse(accountService.existsByEmail("johndoe@example.com"));

        // Securing GET /login/oauth2/code/github?code=fake-code&state=some-state
        mockMvc.perform(get("/login/oauth2/code/github?code=" + code + "&state=" + state)
            .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/workspaces"));

        assertTrue(accountService.existsByEmail("johndoe@example.com"));
    }


    @Test
    void testGithubOAuth2AuthenticationExistingUser() throws Exception {

        var existingAccount = new SignupAccount(
            "johndoe",
            "johndoe@example.com",
            "fake-password",
            "John",
            "Doe",
            "email"
        );
        accountService.signup(existingAccount);

        // Securing GET /oauth2/authorization/github
        var result = mockMvc.perform(get("/oauth2/authorization/github"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", startsWith(wireMockExtension.baseUrl() + "/login/oauth/authorize")))
            .andExpect(header().string("Location", containsString("client_id=fake-client-id")))
            .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);

        String encodedState = fromUriString(location).build().getQueryParams().getFirst("state");
        assertNotNull(encodedState);
        String state = URLDecoder.decode(encodedState, StandardCharsets.UTF_8);

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session);

        // HTTP POST https://github.com/login/oauth/access_token
        wireMockExtension.stubFor(WireMock.post(urlPathEqualTo("/login/oauth/access_token"))
            .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "access_token": "fake-access-token",
                        "token_type": "bearer"
                    }
                    """)));

        // HTTP GET https://api.github.com/user
        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/user"))
            .withHeader("Authorization", equalTo("Bearer " + accessToken))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "login": "johndoe",
                        "name": "John Doe",
                        "email": "johndoe@example.com"
                    }
                    """)));

        // Securing GET /login/oauth2/code/github?code=fake-code&state=some-state
        mockMvc.perform(get("/login/oauth2/code/github?code=" + code + "&state=" + state)
            .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/workspaces"));

        assertEquals(1, accountService.findAll().size());
    }

    @Test
    void testGithubOAuth2AuthenticationInvalidState() throws Exception {

        // Securing GET /oauth2/authorization/github
        var result = mockMvc.perform(get("/oauth2/authorization/github"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", startsWith(wireMockExtension.baseUrl() + "/login/oauth/authorize")))
            .andExpect(header().string("Location", containsString("client_id=fake-client-id")))
            .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session);

        String invalidState = "invalid-state";

        // Securing GET /login/oauth2/code/github?code=fake-code&state=some-state
        mockMvc.perform(get("/login/oauth2/code/github?code=" + code + "&state=" + invalidState)
            .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));

        assertEquals("[authorization_request_not_found]", session.getAttribute("errorMessage"));
    }

    @Test
    void testGithubOAuth2AuthenticationNoEmailFromProvider() throws Exception {

        // Securing GET /oauth2/authorization/github
        var result = mockMvc.perform(get("/oauth2/authorization/github"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", startsWith(wireMockExtension.baseUrl() + "/login/oauth/authorize")))
            .andExpect(header().string("Location", containsString("client_id=fake-client-id")))
            .andReturn();

        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);

        String encodedState = fromUriString(location).build().getQueryParams().getFirst("state");
        assertNotNull(encodedState);
        String state = URLDecoder.decode(encodedState, StandardCharsets.UTF_8);

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();
        assertNotNull(session);

        // HTTP POST https://github.com/login/oauth/access_token
        wireMockExtension.stubFor(WireMock.post(urlPathEqualTo("/login/oauth/access_token"))
            .withHeader("Content-Type", containing("application/x-www-form-urlencoded"))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "access_token": "fake-access-token",
                        "token_type": "bearer"
                    }
                    """)));

        // HTTP GET https://api.github.com/user
        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/user"))
            .withHeader("Authorization", equalTo("Bearer " + accessToken))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "login": "johndoe",
                        "name": "John Doe",
                        "email": null
                    }
                    """)));

        // HTTP GET https://api.github.com/user/emails
        wireMockExtension.stubFor(WireMock.get(urlPathEqualTo("/user/emails"))
            .withHeader("Authorization", equalTo("Bearer " + accessToken))
            .willReturn(aResponse().withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[]")));

        // Securing GET /login/oauth2/code/github?code=fake-code&state=some-state
        mockMvc.perform(get("/login/oauth2/code/github?code=" + code + "&state=" + state)
            .session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login?error"));

        assertEquals("Email from provider GITHUB not received", session.getAttribute("errorMessage"));
    }
}

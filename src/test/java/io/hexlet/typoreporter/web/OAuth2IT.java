package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.account.OAuth2GithubUser;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.service.AccountService;
import io.hexlet.typoreporter.service.dto.oauth2.PrivateEmail;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import java.util.Set;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
public class OAuth2IT {
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void updateAccountWithOAuth2AuthenticationSuccess() throws Exception {
        assertThat(accountRepository.findAccountByEmail("test@gmail.com")).isEmpty();
        OAuth2GithubUser user = getOAuth2GithubUser();
        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(user,
            Set.of(new SimpleGrantedAuthority("read:user"), new SimpleGrantedAuthority("user:email")),
            "github");
        SecurityContextHolder.getContext().setAuthentication(token);
        accountService.createGithubUser(user);
        assertThat(accountRepository.findAccountByEmail("test@gmail.com")).isNotEmpty();

        mockMvc.perform(put("/account/update")
            .param("firstName", "Тестер2")
            .param("lastName", "Тестеров2")
            .param("username", "test2")
            .param("email", "test@gmail.com")
            .with(user("test@gmail.com"))
            .with(csrf()))
            .andExpect(status().isFound());
        assertThat(accountRepository.findAccountByEmail("test@gmail.com").get().getFirstName()).isEqualTo("Тестер2");
    }

    @NotNull
    private static OAuth2GithubUser getOAuth2GithubUser() {
        OAuth2User oAuth2User = new DefaultOAuth2User(
            Set.of(new SimpleGrantedAuthority("ROLE_USER")),
            Map.of("email", "test@gmail.com",
                "name", "Тестер Тестеров",
                "login", "test",
                "id", 123456789),
            "id"
        );
        PrivateEmail email = new PrivateEmail();
        email.setEmail("test@gmail.com");
        email.setVerified(true);
        email.setPrimary(true);
        email.setVisibility("");
        return new OAuth2GithubUser(oAuth2User, email);
    }
}

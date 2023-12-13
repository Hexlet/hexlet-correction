package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
public class AccountControllerIT {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
        .withPassword("inmemory")
        .withUsername("inmemory");

    @DynamicPropertySource
    static void dataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void updateAccountWithWrongEmailDomain() throws Exception {
        String userName = "testUser";
        String correctEmailDomain = "test@test.test";
        String password = "_Qwe1234";
        mockMvc.perform(post("/signup")
            .param("username", userName)
            .param("email", correctEmailDomain)
            .param("password", password)
            .param("confirmPassword", password)
            .param("firstName", userName)
            .param("lastName", userName)
            .with(csrf()));
        assertThat(accountRepository.findAccountByEmail(correctEmailDomain)).isNotEmpty();
        assertThat(accountRepository.findAccountByEmail(correctEmailDomain).orElseThrow().getEmail())
            .isEqualTo(correctEmailDomain);

        String wrongEmailDomain = "test@test";
        mockMvc.perform(post("/account/update")
            .param("username", userName)
            .param("email", wrongEmailDomain)
            .param("password", password)
            .param("confirmPassword", password)
            .param("firstName", userName)
            .param("lastName", userName)
            .with(csrf()));
        assertThat(accountRepository.findAccountByEmail(wrongEmailDomain)).isEmpty();
        assertThat(accountRepository.findAccountByEmail(correctEmailDomain).orElseThrow().getEmail())
            .isEqualTo(correctEmailDomain);
    }

    @Test
    void updateAccountEmailUsingDifferentCase() throws Exception {
        final String username = "testUser";
        final String emailUpperCase = "TEST@TEST.RU";
        final String emailMixedCase = "TEST@test.Ru";
        final String emailLowerCase = "test@test.ru";
        final String password = "_Qwe1234";

        mockMvc.perform(post("/signup")
            .param("username", username)
            .param("email", emailMixedCase)
            .param("password", password)
            .param("confirmPassword", password)
            .param("firstName", username)
            .param("lastName", username)
            .with(csrf()));
        assertThat(accountRepository.findAccountByEmail(emailLowerCase)).isNotEmpty();

        mockMvc.perform(put("/account/update")
            .param("firstName", username)
            .param("lastName", username)
            .param("username", username)
            .param("email", emailUpperCase)
            .with(csrf()));
        assertThat(accountRepository.findAccountByEmail(emailUpperCase)).isEmpty();
        assertThat(accountRepository.findAccountByEmail(emailLowerCase)).isNotEmpty();
    }
}

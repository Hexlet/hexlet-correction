package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import io.hexlet.typoreporter.web.model.SignupAccountModel;
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

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
class LoginIT {

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
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private static final String EMAIL_UPPER_CASE = "EMAIL_ADDRESS@GOOGLE.COM";
    private static final String EMAIL_LOWER_CASE = EMAIL_UPPER_CASE.toLowerCase();

    private final SignupAccountModel model = new SignupAccountModel(
        "model_upper_case",
        EMAIL_UPPER_CASE,
        "password", "password",
        "firstName", "lastName",
        "EMAIL");

    @Test
    void loginByEmailInAnyCaseSuccess() throws Exception {
        assertThat(accountRepository.findAccountByEmail(EMAIL_LOWER_CASE)).isEmpty();

        mockMvc.perform(post("/signup")
            .param("username", model.getUsername())
            .param("email", model.getEmail())
            .param("password", model.getPassword())
            .param("confirmPassword", model.getPassword())
            .param("firstName", model.getFirstName())
            .param("lastName", model.getLastName())
            .with(csrf()));
        assertThat(accountRepository.findAccountByEmail(EMAIL_LOWER_CASE)).isNotEmpty();

        String locationHeaderStrLowerCaseEmail = mockMvc.perform(post("/login")
                .param("email", EMAIL_LOWER_CASE)
                .param("password", model.getPassword())
                .with(csrf()))
            .andReturn()
            .getResponse()
            .getHeader("Location");
        assertThat(locationHeaderStrLowerCaseEmail).contains("workspaces");

        String locationHeaderStrBadEmail = mockMvc.perform(post("/login")
            .param("email", "bad@email.com")
            .param("password", model.getPassword())
            .with(csrf())).andReturn().getResponse().getHeader("Location");
        assertThat(locationHeaderStrBadEmail).doesNotContain("workspaces");
        assertThat(locationHeaderStrBadEmail).contains("login");

        String locationHeaderStrUpperCaseEmail = mockMvc.perform(post("/login")
            .param("email", EMAIL_UPPER_CASE)
            .param("password", model.getPassword())
            .with(csrf())
        ).andReturn().getResponse().getHeader("Location");
        assertThat(locationHeaderStrUpperCaseEmail).contains("workspaces");
    }
}

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
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
class SignupControllerIT {

    private final String EMAIL_IN_UPPER_CASE = "USERNAME@google.com";
    private final String EMAIL_IN_LOWER_CASE = "username@google.com";

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

    private SignupAccountModel model = new SignupAccountModel(
        "username",
        EMAIL_IN_UPPER_CASE, EMAIL_IN_UPPER_CASE,
        "password","password",
        "firstName", "lastName");

    private SignupAccountModel anotherModelWithSameButLowerCaseEmail = new SignupAccountModel(
        "ANOTHER_username",
        EMAIL_IN_LOWER_CASE, EMAIL_IN_LOWER_CASE,
        "ANOTHER_password", "ANOTHER_password",
        "ANOTHER_firstName", "ANOTHER_lastName");

    @Test
    void createAccountWithIgnoreEmailCase() throws Exception {
        mockMvc.perform(post("/signup")
            .param("username", model.getUsername())
            .param("email", EMAIL_IN_UPPER_CASE)
            .param("confirmEmail", EMAIL_IN_UPPER_CASE)
            .param("password", model.getPassword())
            .param("confirmPassword", model.getConfirmPassword())
            .param("firstName", model.getFirstName())
            .param("lastName", model.getLastName())
            .with(csrf()));
        assertThat(accountRepository.findAccountByUsername("username")).isNotEmpty();

        mockMvc.perform(post("/signup")
            .param("username", anotherModelWithSameButLowerCaseEmail.getUsername())
            .param("email", EMAIL_IN_LOWER_CASE)
            .param("confirmEmail", EMAIL_IN_LOWER_CASE)
            .param("password", anotherModelWithSameButLowerCaseEmail.getPassword())
            .param("confirmPassword", anotherModelWithSameButLowerCaseEmail.getConfirmPassword())
            .param("firstName", anotherModelWithSameButLowerCaseEmail.getFirstName())
            .param("lastName", anotherModelWithSameButLowerCaseEmail.getLastName())
            .with(csrf()));
        assertThat(accountRepository.findAccountByUsername("ANOTHER_username")).isEmpty();
    }
}

package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.domain.account.Account;
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

import java.util.Optional;

import static com.github.database.rider.core.api.configuration.Orthography.LOWERCASE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;
import static io.hexlet.typoreporter.test.Constraints.POSTGRES_IMAGE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
@DBRider
@DBUnit(caseInsensitiveStrategy = LOWERCASE, dataTypeFactoryClass = DBUnitEnumPostgres.class, cacheConnection = false)
class SignupControllerIT {

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

    private final String EMAIL_UPPER_CASE = "EMAIL_ADDRESS@GOOGLE.COM";
    private final String EMAIL_LOWER_CASE = EMAIL_UPPER_CASE.toLowerCase();

    private SignupAccountModel model = new SignupAccountModel(
        "model_upper_case",
        EMAIL_UPPER_CASE, EMAIL_UPPER_CASE,
        "password","password",
        "firstName", "lastName");

    private SignupAccountModel anotherModelWithSameButLowerCaseEmail = new SignupAccountModel(
        "model_lower_case",
        EMAIL_LOWER_CASE, EMAIL_LOWER_CASE,
        "another_password", "another_password",
        "another_firstName", "another_lastName");

    @Test
    void createAccountWithIgnoreEmailCase() throws Exception {
        mockMvc.perform(post("/signup")
            .param("username", model.getUsername())
            .param("email", model.getEmail())
            .param("confirmEmail", model.getEmail())
            .param("password", model.getPassword())
            .param("confirmPassword", model.getConfirmPassword())
            .param("firstName", model.getFirstName())
            .param("lastName", model.getLastName())
            .with(csrf()));
        assertThat(accountRepository.findAccountByUsername("model_upper_case")).isNotEmpty();
        assertThat(accountRepository.findAccountByUsername("model_upper_case").orElseThrow().getEmail())
            .isEqualTo(EMAIL_LOWER_CASE);

        mockMvc.perform(post("/signup")
            .param("username", anotherModelWithSameButLowerCaseEmail.getUsername())
            .param("email", anotherModelWithSameButLowerCaseEmail.getEmail())
            .param("confirmEmail", anotherModelWithSameButLowerCaseEmail.getEmail())
            .param("password", anotherModelWithSameButLowerCaseEmail.getPassword())
            .param("confirmPassword", anotherModelWithSameButLowerCaseEmail.getConfirmPassword())
            .param("firstName", anotherModelWithSameButLowerCaseEmail.getFirstName())
            .param("lastName", anotherModelWithSameButLowerCaseEmail.getLastName())
            .with(csrf()));
        assertThat(accountRepository.findAccountByUsername("model_lower_case")).isEmpty();
    }

    @Test
    void createAccountWithWrongEmailDomain() throws Exception {
        String userName = "testUser";
        String password = "_Qwe1234";
        String wrongEmailDomain = "test@test";
        mockMvc.perform(post("/signup")
            .param("username", userName)
            .param("email", wrongEmailDomain)
            .param("confirmEmail", wrongEmailDomain)
            .param("password", password)
            .param("confirmPassword", password)
            .param("firstName", userName)
            .param("lastName", userName)
            .with(csrf()));
        assertThat(accountRepository.findAccountByEmail(wrongEmailDomain)).isEmpty();
    }

    void createAccountWithWrongPassword() throws Exception {
        String userName = "testUser";
        String wrongPass = "pass";
        String email = "test@test.ru";
        mockMvc.perform(post("/signup")
            .param("username", userName)
            .param("email", email)
            .param("confirmEmail", email)
            .param("password", wrongPass)
            .param("confirmPassword", wrongPass)
            .param("firstName", userName)
            .param("lastName", userName)
            .with(csrf())).andExpect((status().isFound()));
        assertThat(accountRepository.findAccountByEmail(email)).isEmpty();

    }

    //my add
    @Test
    void createAccountWithDifferentCaseInEmailAndConfirmation() throws Exception {
        assertThat(accountRepository.findAccountByUsername(model.getUsername())).isEmpty();

        mockMvc.perform(post("/signup")
            .param("username", model.getUsername())
            .param("email", model.getEmail())
            .param("confirmEmail", anotherModelWithSameButLowerCaseEmail.getEmail())
            .param("password", model.getPassword())
            .param("confirmPassword", model.getConfirmPassword())
            .param("firstName", model.getFirstName())
            .param("lastName", model.getLastName())
            .with(csrf()));
        Optional<Account> addedAcc = accountRepository.findAccountByUsername(model.getUsername());
        assertThat(addedAcc).isNotEmpty();
        assertThat(addedAcc.orElseThrow().getEmail())
            .isEqualTo(anotherModelWithSameButLowerCaseEmail.getEmail());
    }
    //my add end
}

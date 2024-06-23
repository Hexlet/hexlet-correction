package io.hexlet.typoreporter.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.spring.api.DBRider;
import io.hexlet.typoreporter.repository.AccountRepository;
import io.hexlet.typoreporter.test.DBUnitEnumPostgres;
import io.hexlet.typoreporter.test.factory.AccountModelGenerator;
import io.hexlet.typoreporter.utils.BundleSourceUtils;
import io.hexlet.typoreporter.utils.ModelUtils;
import io.hexlet.typoreporter.web.model.SignupAccountModel;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private AccountModelGenerator accountGenerator;

    @Autowired
    private ModelUtils modelUtils;
    @Autowired
    private BundleSourceUtils bundleSourceUtils;

    @Test
    void updateAccountWithWrongEmailDomain() throws Exception {
        SignupAccountModel correctAccount = Instancio.of(accountGenerator.getCorrectAccountModel()).create();
        var correctFormParams = modelUtils.toFormParams(correctAccount);
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(correctFormParams)
                .with(csrf()))
            .andExpect(status().isFound());
        assertThat(accountRepository.findAccountByEmail(correctAccount.getEmail())).isNotEmpty();
        assertThat(accountRepository.findAccountByEmail(correctAccount.getEmail()).orElseThrow().getEmail())
            .isEqualTo(correctAccount.getEmail());

        SignupAccountModel incorrectAccount = Instancio.of(accountGenerator.getIncorrectAccountModel()).create();
        var incorrectFormParams = modelUtils.toFormParams(incorrectAccount);
        var response = mockMvc.perform(put("/account/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(incorrectFormParams)
                .with(user(correctAccount.getEmail()))
                .with(csrf()))
            .andExpect(status().isOk())
            .andReturn();

        var body = response.getResponse().getContentAsString();
        assertThat(accountRepository.findAccountByEmail(incorrectAccount.getEmail())).isEmpty();
        assertThat(accountRepository.findAccountByEmail(correctAccount.getEmail()).orElseThrow().getEmail())
            .isEqualTo(correctAccount.getEmail());
        assertThat(body).contains(bundleSourceUtils.getValueByKey(
            "validation.alert.wrong-email", true, new String[]{incorrectAccount.getEmail()}));
        assertThat(body).contains(bundleSourceUtils.getValueByKey(
            "validation.alert.wrong-username-pattern", true, new String[]{"^[-_A-Za-z0-9]*$"}));
    }

    @Test
    void updateAccountEmailUsingDifferentCase() throws Exception {
        final SignupAccountModel account = Instancio.of(accountGenerator.getCorrectAccountModel()).create();
        account.setEmail(account.getEmail().toUpperCase());
        var accountFormParams = modelUtils.toFormParams(account);
        mockMvc.perform(post("/signup")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(accountFormParams)
            .with(csrf()))
            .andExpect(status().isFound());
        assertThat(accountRepository.findAccountByEmail(account.getEmail().toLowerCase())).isNotEmpty();

        final SignupAccountModel accountToUpdate = Instancio.of(accountGenerator.getCorrectAccountModel()).create();
        accountToUpdate.setEmail(account.getEmail().toLowerCase());
        accountFormParams = modelUtils.toFormParams(accountToUpdate);
        mockMvc.perform(put("/account/update")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(accountFormParams)
            .with(user(account.getEmail()))
            .with(csrf()))
            .andExpect(status().isOk());
        assertThat(accountRepository.findAccountByEmail(accountToUpdate.getEmail().toUpperCase())).isEmpty();
        assertThat(accountRepository.findAccountByEmail(accountToUpdate.getEmail())).isNotEmpty();
    }
}

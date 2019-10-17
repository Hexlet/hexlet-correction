package io.hexlet.hexletcorrection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerStaticTest extends AbstractControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    private Account testAccountOne;
    private Account testAccountTwo;

    private final String CONTENT_TYPE = ContentType.HTML.withCharset(StandardCharsets.UTF_8);

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
        testAccountOne = createAccount("testAccountOne", "testAccountOne@email.com");
        testAccountTwo = createAccount("testAccountTwo", "testAccountTwo@email.com");
    }

    @After
    public void tearDown() {
        deleteAccount(testAccountOne.getId());
        deleteAccount(testAccountTwo.getId());
        testAccountOne = null;
        testAccountTwo = null;
    }

    @WithMockUser
    @Test
    public void getAccounts() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getName()))))
            .andExpect(content().string(containsString(format("<td>%d</td>", testAccountOne.getId()))))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getEmail()))))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountTwo.getName()))))
            .andExpect(content().string(containsString(format("<td>%d</td>", testAccountTwo.getId()))))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountTwo.getEmail()))));
    }

    @WithMockUser
    @Test
    public void getAccountByIdNotFound() throws Exception {
        final Long accountId = testAccountOne.getId() + testAccountTwo.getId();
        mvc.perform(get(ACCOUNTS_PATH + "/" + accountId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("Account with id=%d not found", accountId))));
    }

    @WithMockUser
    @Test
    public void getAccountById() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH + "/" + testAccountOne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getName()))))
            .andExpect(content().string(containsString(format("<td>%d</td>", testAccountOne.getId()))))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getEmail()))));
    }

    @WithMockUser
    @Test
    public void getAccountByName() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH).param("name", testAccountOne.getName()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getName()))))
            .andExpect(content().string(containsString(format("<td>%d</td>", testAccountOne.getId()))))
            .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getEmail()))))
            .andExpect(content().string(not(containsString(format("<td>%s</td>", testAccountTwo.getName())))))
            .andExpect(content().string(not(containsString(format("<td>%d</td>", testAccountTwo.getId())))))
            .andExpect(content().string(not(containsString(format("<td>%s</td>", testAccountTwo.getEmail())))));
    }

    @WithMockUser
    @Test
    public void getCreationForm() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH + "/new"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE));
    }

    @WithMockUser
    @Test
    public void postAccount() throws Exception {
        String testEmail = "test@email.com";

        mvc.perform(
            post(ACCOUNTS_PATH)
                .param("email", testEmail)
                .param("name", DEFAULT_USER_NAME)
                .param("password", DEFAULT_USER_PASSWORD)
                .param("passwordConfirm", DEFAULT_USER_PASSWORD)
                .with(csrf())
        ).andExpect(status().isFound());

        Account actualAccount = accountService.findByEmail(testEmail).orElse(null);
        assertNotNull(actualAccount);
        assertEquals(actualAccount.getEmail(), testEmail);
        assertEquals(actualAccount.getName(), DEFAULT_USER_NAME);
    }

    @WithMockUser
    @Test
    public void getEditForm() throws Exception {
        mvc.perform(get(format("%s/edit/%d", ACCOUNTS_PATH, testAccountOne.getId())))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("name=\"name\" value=\"%s\">", testAccountOne.getName()))))
            .andExpect(content().string(containsString(format("name=\"email\" value=\"%s\">", testAccountOne.getEmail()))));
    }

    @WithMockUser
    @Test
    public void putAccount() throws Exception {
        final String newName = "new" + testAccountOne.getName();
        final String newEmail = "new" + testAccountOne.getEmail();
        final Long accountId = testAccountOne.getId();

        AccountDto accountDto = AccountDto.builder()
            .id(accountId)
            .name(newName)
            .email(newEmail)
            .corrections(Collections.emptySet())
            .build();

        mvc.perform(
            put(ACCOUNTS_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto))
                .with(csrf())
        ).andExpect(status().isFound());

        Account accountActual = accountService.findById(accountId).orElse(null);
        assertNotNull(accountActual);
        assertEquals(accountActual.getEmail(), newEmail);
        assertEquals(accountActual.getName(), newName);
        assertEquals(accountActual.getId(), accountId);
    }

    @WithMockUser
    @Test
    public void deleteAccount() throws Exception {
        Long idDeleted = testAccountOne.getId();
        Long idNotDeleted = testAccountTwo.getId();
        mvc.perform(delete(ACCOUNTS_PATH + "/" + idDeleted).with(csrf()))
            .andExpect(status().isFound());
        Account accountDeleted = accountService.findById(idDeleted).orElse(null);
        Account accountNotDeleted = accountService.findById(idNotDeleted).orElse(null);
        assertNull(accountDeleted);
        assertNotNull(accountNotDeleted);
    }
}

package io.hexlet.hexletcorrection.controller.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.hexletcorrection.controller.AbstractControllerTest;
import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.restassured.http.ContentType;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
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
public class AccountControllerWebTest extends AbstractControllerTest {

    private static long EXISTED_USER_ID = 1L;

    private static long NON_EXISTED_USER_ID = 1000L;

    private static String DEFAULT_USER_EMAIL = "testAccounts@mail.com";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mvc;

    private final String CONTENT_TYPE = ContentType.HTML.withCharset(StandardCharsets.UTF_8);

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
        when(accountService.findByEmail(any())).thenReturn(Optional.of(createAccount(DEFAULT_USER_NAME, DEFAULT_USER_EMAIL)));

        when(accountService.findById(EXISTED_USER_ID)).thenReturn(Optional.of(createAccountWithId(EXISTED_USER_ID, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL)));
        when(accountService.findByEmail(any())).thenReturn(Optional.of(createAccount(DEFAULT_USER_NAME, DEFAULT_USER_EMAIL)));
        when(accountService.create(any())).thenReturn(createAccountWithId(EXISTED_USER_ID, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL));
    }

    @WithMockUser
    @Test
    public void getAccounts() throws Exception {
        when(accountService.findAll()).thenReturn(Arrays.asList(
            createAccountWithId(EXISTED_USER_ID, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL),
            createAccountWithId(EXISTED_USER_ID + 1, DEFAULT_USER_NAME + 1, "testAccounts1@mail.com")));

        mvc.perform(get(ACCOUNTS_PATH))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_NAME))))
            .andExpect(content().string(containsString(format("<td>%d</td>", EXISTED_USER_ID))))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_EMAIL))))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_NAME + 1))))
            .andExpect(content().string(containsString(format("<td>%d</td>", EXISTED_USER_ID + 1))))
            .andExpect(content().string(containsString(format("<td>%s</td>", "testAccounts1@mail.com"))));
    }

    @WithMockUser
    @Test
    public void getAccountByIdNotFound() throws Exception {
        when(accountService.findById(NON_EXISTED_USER_ID)).thenReturn(Optional.empty());

        mvc.perform(get(ACCOUNTS_PATH + "/" + NON_EXISTED_USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("Account with id=%d not found", NON_EXISTED_USER_ID))));
    }

    @WithMockUser
    @Test
    public void getAccountById() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH + "/" + EXISTED_USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_NAME))))
            .andExpect(content().string(containsString(format("<td>%d</td>", EXISTED_USER_ID))))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_EMAIL))));
    }

    @WithMockUser
    @Test
    public void getAccountByName() throws Exception {
        when(accountService.findByName(DEFAULT_USER_NAME)).thenReturn(Arrays.asList(createAccountWithId(EXISTED_USER_ID, DEFAULT_USER_NAME, DEFAULT_USER_EMAIL)));

        mvc.perform(get(ACCOUNTS_PATH).param("name", DEFAULT_USER_NAME))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_NAME))))
            .andExpect(content().string(containsString(format("<td>%d</td>", EXISTED_USER_ID))))
            .andExpect(content().string(containsString(format("<td>%s</td>", DEFAULT_USER_EMAIL))));
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
        mvc.perform(
            post(ACCOUNTS_PATH)
                .param("email", DEFAULT_USER_EMAIL)
                .param("name", DEFAULT_USER_NAME)
                .param("password", DEFAULT_USER_PASSWORD)
                .param("passwordConfirm", DEFAULT_USER_PASSWORD)
                .with(csrf())
        ).andExpect(status().isFound());

        Account actualAccount = accountService.findByEmail(DEFAULT_USER_EMAIL).orElse(null);
        assertNotNull(actualAccount);
        assertEquals(actualAccount.getEmail(), DEFAULT_USER_EMAIL);
        assertEquals(actualAccount.getName(), DEFAULT_USER_NAME);
    }

    @WithMockUser
    @Test
    public void getEditForm() throws Exception {
        mvc.perform(get(format("%s/edit/%d", ACCOUNTS_PATH, EXISTED_USER_ID)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(CONTENT_TYPE))
            .andExpect(content().string(containsString(format("name=\"name\" value=\"%s\">", DEFAULT_USER_NAME))))
            .andExpect(content().string(containsString(format("name=\"email\" value=\"%s\">", DEFAULT_USER_EMAIL))));
    }

    @WithMockUser
    @Test
    public void putAccount() throws Exception {
        final String newName = "new" + DEFAULT_USER_NAME;
        final String newEmail = "new" + DEFAULT_USER_EMAIL;
        final Long accountId = EXISTED_USER_ID;

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

        when(accountService.findById(EXISTED_USER_ID)).thenReturn(Optional.of(createAccountWithId(EXISTED_USER_ID, newName, newEmail)));

        Account accountActual = accountService.findById(accountId).orElse(null);
        assertNotNull(accountActual);
        assertEquals(newEmail, accountActual.getEmail());
        assertEquals(newName, accountActual.getName());
        assertEquals(accountId, accountActual.getId());
    }

    @WithMockUser
    @Test
    public void deleteAccount() throws Exception {
        doNothing().when(accountService).delete(any());
        mvc.perform(delete(ACCOUNTS_PATH + "/" + EXISTED_USER_ID).with(csrf()))
            .andExpect(status().isFound());
    }
}

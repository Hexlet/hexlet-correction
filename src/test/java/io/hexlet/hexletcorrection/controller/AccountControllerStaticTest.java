package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Account;
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

import static io.hexlet.hexletcorrection.controller.ControllerConstants.ACCOUNTS_PATH;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.containsString;
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

    private MockMvc mvc;


    private final String CONTENT_TYPE = ContentType.HTML.withCharset(StandardCharsets.UTF_8);

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser("spring")
    @Test
    public void getAccounts() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));
    }

    @WithMockUser("spring")
    @Test
    public void getAccountByIdNotFound() throws Exception {
        final Long accountId = 1L;
        mvc.perform(get(ACCOUNTS_PATH + "/" + accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().string(containsString(format("Account with id=%d not found", accountId))));
    }

    @WithMockUser("spring")
    @Test
    public void getAccountById() throws Exception {
        Account testAccountOne = createAccount("testAccountOne", "testAccountOne@email.com");
        mvc.perform(get(ACCOUNTS_PATH + "/" + testAccountOne.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getName()))))
                .andExpect(content().string(containsString(format("<td>%d</td>", testAccountOne.getId()))))
                .andExpect(content().string(containsString(format("<td>%s</td>", testAccountOne.getEmail()))));
    }

    @WithMockUser("spring")
    @Test
    public void getCreationForm() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH + "/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));
    }

    @WithMockUser("spring")
    @Test
    public void postAccount() throws Exception {
        mvc.perform(post(ACCOUNTS_PATH))
                .andExpect(status().isFound());
    }

    @WithMockUser("spring")
    @Test
    public void getEditForm() throws Exception {
        mvc.perform(get(ACCOUNTS_PATH + "/edit/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));
    }

    @WithMockUser("spring")
    @Test
    public void putAccount() throws Exception {
        mvc.perform(put(ACCOUNTS_PATH))
                .andExpect(status().isFound());
    }

    @WithMockUser("spring")
    @Test
    public void deleteAccount() throws Exception {
        Account testAccountOne = createAccount("testAccountOne", "testAccountOne@email.com");
        mvc.perform(delete(ACCOUNTS_PATH + "/" + testAccountOne.getId()))
                .andExpect(status().isFound());
    }
}
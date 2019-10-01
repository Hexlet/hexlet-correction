package io.hexlet.hexletcorrection.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.hexlet.hexletcorrection.controller.ControllerConstants.PROFILE_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.PROFILE_SETTINGS_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SCRIPT_PATH;
import static io.hexlet.hexletcorrection.controller.ControllerConstants.SERVICE_HOST;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfileControllerTest extends AbstractControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser("profilePageTest@email.com")
    public void profilePageTest() throws Exception {
        final String email = "profilePageTest@email.com";
        createAccount(DEFAULT_USER_NAME, email);

        mockMvc.perform(get(PROFILE_PATH).contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(DEFAULT_USER_NAME)));
    }

    @Test
    @WithMockUser("profileSettingsPageTest@email.com")
    public void profileSettingsPageTest() throws Exception {
        final String email = "profileSettingsPageTest@email.com";
        createAccount(DEFAULT_USER_NAME, email);

        mockMvc.perform(get(PROFILE_SETTINGS_PATH).contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString(SERVICE_HOST + SCRIPT_PATH)));
    }
}

package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.service.AccountService;
import io.hexlet.hexletcorrection.service.CorrectionService;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

public abstract class AbstractControllerTest {

    protected final String DEFAULT_USER_NAME = "Test user";
    protected final String DEFAULT_USER_PASSWORD = "password";

    @MockBean
    protected AccountService accountService;

    @MockBean
    protected CorrectionService correctionService;

    protected Account createAccount(String name, String email) {
        return Account.builder()
            .name(name)
            .password(DEFAULT_USER_PASSWORD)
            .email(email)
            .corrections(Collections.emptySet())
            .build();
    }

    protected Account createAccountWithId(Long id, String name, String email) {
        Account account = createAccount(name, email);
        account.setId(id);
        return account;
    }

    protected Correction createCorrection(Account account) {
        return Correction.builder()
            .comment("test comment")
            .highlightText("text to correction")
            .pageURL("hexlet.io")
            .account(account)
            .build();
    }

    protected Correction createCorrectionWithId(Long id, Account account) {
        Correction correction = createCorrection(account);
        correction.setId(id);
        return correction;
    }
}

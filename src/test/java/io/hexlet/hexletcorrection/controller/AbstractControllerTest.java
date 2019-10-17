package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.service.AccountService;
import io.hexlet.hexletcorrection.service.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractControllerTest {

    protected final String DEFAULT_USER_NAME = "Test user";

    protected final String DEFAULT_USER_PASSWORD = "password";

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected CorrectionService correctionService;

    protected Account createAccount(String name, String email) {
        Account account = Account.builder()
                .name(name)
                .password(DEFAULT_USER_PASSWORD)
                .email(email)
                .build();

        return accountService.create(account);
    }

    protected Correction createCorrection(Account account) {
        Correction correction = Correction.builder()
                .comment("test comment")
                .highlightText("text to correction")
                .pageURL("hexlet.io")
                .account(account)
                .build();

        return correctionService.create(correction);
    }

    protected void deleteAccount(final Long id) {
        accountService.delete(id);
    }
}

package io.hexlet.hexletcorrection.controller;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.service.AccountService;
import io.hexlet.hexletcorrection.service.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractControlerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CorrectionService correctionService;

    protected Account createAccount() {
        Account account = Account.builder()
                .name("Artem")
                .email("artem@hexlet.io")
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
}

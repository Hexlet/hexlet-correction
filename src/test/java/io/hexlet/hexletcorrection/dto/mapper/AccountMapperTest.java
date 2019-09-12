package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountMapperTest {

    @Test
    public void accountToAccountDtoTest() {
        final AccountDto expectedAccountDto = getAccountDto();
        final AccountDto actualAccountDto = AccountMapper.INSTANCE.accountToAccountDto(getAccount());

        assertEquals(expectedAccountDto.getId(), actualAccountDto.getId());
        assertEquals(expectedAccountDto.getName(), actualAccountDto.getName());
        assertEquals(expectedAccountDto.getEmail(), actualAccountDto.getEmail());

        assertEquals(expectedAccountDto.getCorrections().size(), actualAccountDto.getCorrections().size());
    }

    @Test
    public void accountDtoToAccountTest() {
        final Account expectedAccount = getAccount();
        final Account actualAccount = AccountMapper.INSTANCE.accountDtoToAccount(getAccountDto());

        assertNull(actualAccount.getId());
        assertNull(actualAccount.getCorrections());
        assertEquals(expectedAccount.getName(), actualAccount.getName());
        assertEquals(expectedAccount.getEmail(), actualAccount.getEmail());
    }

    @Test
    public void accountToAccountDtoNullTest() {
        assertNull(AccountMapper.INSTANCE.accountToAccountDto(null));
    }

    @Test
    public void accountDtoToAccountNullTest() {
        assertNull(AccountMapper.INSTANCE.accountDtoToAccount(null));
    }

    @Test
    public void correctionsToCorrectionsDtoNullTest() {
        assertNull(AccountMapper.INSTANCE.correctionsToCorrectionsDto(null));
    }

    private Account getAccount() {
        final Account account = Account.builder()
                .id(1L)
                .name("Anatoly")
                .email("anatoly@hexlet.io")
                .build();

        final Set<Correction> corrections = Set.of(
                Correction.builder()
                        .id(1L)
                        .comment("Some comment1")
                        .highlightText("some mistake1")
                        .pageURL("https://hexlet.io/test1")
                        .account(account)
                        .build(),
                Correction.builder()
                        .id(3L)
                        .comment("Some comment3")
                        .highlightText("some mistake3")
                        .pageURL("https://hexlet.io/test3")
                        .account(account)
                        .build(),
                Correction.builder()
                        .id(6L)
                        .comment("Some comment6")
                        .highlightText("some mistake6")
                        .pageURL("https://hexlet.io/test6")
                        .account(account)
                        .build()
        );

        account.setCorrections(corrections);

        return account;
    }

    private AccountDto getAccountDto() {
        return AccountDto.builder()
                .id(1L)
                .name("Anatoly")
                .email("anatoly@hexlet.io")
                .corrections(Set.of(
                        CorrectionDto.builder()
                                .id(1L)
                                .comment("Some comment1")
                                .highlightText("some mistake1")
                                .pageURL("https://hexlet.io/test1")
                                .build(),
                        CorrectionDto.builder()
                                .id(3L)
                                .comment("Some comment3")
                                .highlightText("some mistake3")
                                .pageURL("https://hexlet.io/test3")
                                .build(),
                        CorrectionDto.builder()
                                .id(6L)
                                .comment("Some comment6")
                                .highlightText("some mistake6")
                                .pageURL("https://hexlet.io/test6")
                                .build()
                ))
                .build();
    }
}
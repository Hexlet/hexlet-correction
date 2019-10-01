package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.AccountPostDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountMapperTest {

    @Autowired
    private AccountMapper accountMapper;
    
    @Test
    public void accountToAccountDtoTest() {
        final AccountDto expectedAccountDto = getAccountDto();
        final AccountDto actualAccountDto = accountMapper.toAccountDto(getAccount());

        assertEquals(expectedAccountDto.getId(), actualAccountDto.getId());
        assertEquals(expectedAccountDto.getName(), actualAccountDto.getName());
        assertEquals(expectedAccountDto.getEmail(), actualAccountDto.getEmail());

        assertEquals(expectedAccountDto.getCorrections().size(), actualAccountDto.getCorrections().size());
    }

    @Test
    public void accountDtoToAccountTest() {
        final Account expectedAccount = getAccount();
        final Account actualAccount = accountMapper.toAccount(getAccountDto());

        assertNull(actualAccount.getId());
        assertNull(actualAccount.getCorrections());
        assertEquals(expectedAccount.getName(), actualAccount.getName());
        assertEquals(expectedAccount.getEmail(), actualAccount.getEmail());
    }

    @Test
    public void accountToAccountDtoNullTest() {
        assertNull(accountMapper.toAccountDto(null));
    }

    @Test
    public void accountDtoToAccountNullTest() {
        assertNull(accountMapper.toAccount(null));
    }

    @Test
    public void correctionsToCorrectionsDtoNullTest() {
        assertNull(accountMapper.toCorrectionsDto(null));
    }

    @Test
    public void accountPostDtoTest() {
        final Account expectedAccount = Account.builder()
                .name("Anatoly")
                .email("anatoly@hexlet.io")
                .build();
        final Account actualAccount = accountMapper.postDtoToAccount(getAccountPostDto());

        assertEquals(expectedAccount.getName(), actualAccount.getName());
        assertEquals(expectedAccount.getEmail(), actualAccount.getEmail());
        assertNull(actualAccount.getId());
        assertNull(actualAccount.getCorrections());
    }

    @Test
    public void accountPostDtoNullTest() {
        assertNull(accountMapper.postDtoToAccount(null));
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
                        .beforeHighlight("before highlight1")
                        .afterHighlight("after highlight1")
                        .pageURL("https://hexlet.io/test1")
                        .account(account)
                        .build(),
                Correction.builder()
                        .id(3L)
                        .comment("Some comment3")
                        .highlightText("some mistake3")
                        .beforeHighlight("before highlight3")
                        .afterHighlight("after highlight3")
                        .pageURL("https://hexlet.io/test3")
                        .account(account)
                        .build(),
                Correction.builder()
                        .id(6L)
                        .comment("Some comment6")
                        .highlightText("some mistake6")
                        .beforeHighlight("before highlight6")
                        .afterHighlight("after highlight6")
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
                                .beforeHighlight("before highlight1")
                                .afterHighlight("after highlight1")
                                .pageURL("https://hexlet.io/test1")
                                .build(),
                        CorrectionDto.builder()
                                .id(3L)
                                .comment("Some comment3")
                                .highlightText("some mistake3")
                                .beforeHighlight("before highlight3")
                                .afterHighlight("after highlight3")
                                .pageURL("https://hexlet.io/test3")
                                .build(),
                        CorrectionDto.builder()
                                .id(6L)
                                .comment("Some comment6")
                                .highlightText("some mistake6")
                                .beforeHighlight("before highlight6")
                                .afterHighlight("after highlight6")
                                .pageURL("https://hexlet.io/test6")
                                .build()
                ))
                .build();
    }

    private AccountPostDto getAccountPostDto() {
        return AccountPostDto.builder()
                .name("Anatoly")
                .email("anatoly@hexlet.io")
                .build();
    }
}

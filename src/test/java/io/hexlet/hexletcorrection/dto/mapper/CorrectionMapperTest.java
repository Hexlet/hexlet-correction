package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CorrectionMapperTest {

    @Autowired
    private CorrectionMapper correctionMapper;

    @Test
    public void correctionToCorrectionDtoTest() {
        final CorrectionDto expectedCorrectionDto = getCorrectionDto();
        final CorrectionDto actualCorrectionDto = correctionMapper.toCorrectionDto(getCorrection());

        assertEquals(expectedCorrectionDto.getId(), actualCorrectionDto.getId());
        assertEquals(expectedCorrectionDto.getPageURL(), actualCorrectionDto.getPageURL());
        assertEquals(expectedCorrectionDto.getHighlightText(), actualCorrectionDto.getHighlightText());
        assertEquals(expectedCorrectionDto.getComment(), actualCorrectionDto.getComment());
        assertEquals(expectedCorrectionDto.getAccount().getId(), actualCorrectionDto.getAccount().getId());
    }

    @Test
    public void correctionDtoToCorrectionTest() {
        final Correction expectedCorrection = getCorrection();
        final Correction actualCorrection = correctionMapper.toCorrection(getCorrectionDto());

        assertNull(actualCorrection.getId());
        assertEquals(expectedCorrection.getPageURL(), actualCorrection.getPageURL());
        assertEquals(expectedCorrection.getHighlightText(), actualCorrection.getHighlightText());
        assertEquals(expectedCorrection.getComment(), actualCorrection.getComment());
        assertEquals(expectedCorrection.getAccount().getId(), actualCorrection.getAccount().getId());
    }

    @Test
    public void correctionToCorrectionDtoNullTest() {
        assertNull(correctionMapper.toCorrectionDto(null));
    }

    @Test
    public void correctionDtoToCorrectionNullTest() {
        assertNull(correctionMapper.toCorrection(null));
    }

    @Test
    public void accountToAccountDtoNullTest() {
        assertNull(correctionMapper.toAccountDto(null));
    }

    @Test
    public void accountDtoToAccountNullTest() {
        assertNull(correctionMapper.toAccount(null));
    }

    private Correction getCorrection() {
        return Correction.builder()
                .id(1L)
                .pageURL("hexlet.io")
                .highlightText("some mistake")
                .comment("some mistake comment")
                .account(Account
                        .builder()
                        .id(5L)
                        .build())
                .build();
    }

    private CorrectionDto getCorrectionDto() {
        return CorrectionDto.builder()
                .id(1L)
                .pageURL("hexlet.io")
                .highlightText("some mistake")
                .comment("some mistake comment")
                .account(AccountDto.builder()
                        .id(5L)
                        .build())
                .build();
    }
}
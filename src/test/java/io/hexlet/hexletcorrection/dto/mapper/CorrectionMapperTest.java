package io.hexlet.hexletcorrection.dto.mapper;

import io.hexlet.hexletcorrection.domain.Account;
import io.hexlet.hexletcorrection.domain.Correction;
import io.hexlet.hexletcorrection.dto.AccountDto;
import io.hexlet.hexletcorrection.dto.CorrectionDto;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CorrectionMapperTest {

    @Test
    public void correctionToCorrectionDtoTest() {
        final CorrectionDto expectedCorrectionDto = getCorrectionDto();
        final CorrectionDto actualCorrectionDto = CorrectionMapper.INSTANCE.correctionToCorrectionDto(getCorrection());

        assertEquals(expectedCorrectionDto.getId(), actualCorrectionDto.getId());
        assertEquals(expectedCorrectionDto.getPageURL(), actualCorrectionDto.getPageURL());
        assertEquals(expectedCorrectionDto.getHighlightText(), actualCorrectionDto.getHighlightText());
        assertEquals(expectedCorrectionDto.getComment(), actualCorrectionDto.getComment());
        assertEquals(expectedCorrectionDto.getAccount().getId(), actualCorrectionDto.getAccount().getId());
    }

    @Test
    public void correctionDtoToCorrectionTest() {
        final Correction expectedCorrection = getCorrection();
        final Correction actualCorrection = CorrectionMapper.INSTANCE.correctionDtoToCorrection(getCorrectionDto());

        assertNull(actualCorrection.getId());
        assertEquals(expectedCorrection.getPageURL(), actualCorrection.getPageURL());
        assertEquals(expectedCorrection.getHighlightText(), actualCorrection.getHighlightText());
        assertEquals(expectedCorrection.getComment(), actualCorrection.getComment());
        assertEquals(expectedCorrection.getAccount().getId(), actualCorrection.getAccount().getId());
    }

    @Test
    public void correctionToCorrectionDtoNullTest() {
        assertNull(CorrectionMapper.INSTANCE.correctionToCorrectionDto(null));
    }

    @Test
    public void correctionDtoToCorrectionNullTest() {
        assertNull(CorrectionMapper.INSTANCE.correctionDtoToCorrection(null));
    }

    @Test
    public void accountToAccountDtoNullTest() {
        assertNull(CorrectionMapper.INSTANCE.accountToAccountDto(null));
    }

    @Test
    public void accountDtoToAccountNullTest() {
        assertNull(CorrectionMapper.INSTANCE.accountDtoToAccount(null));
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
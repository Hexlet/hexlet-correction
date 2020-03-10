package io.hexlet.hexletcorrection.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CorrectionMapperTest {

    private CorrectionMapper correctionMapper;

    @BeforeEach
    public void setUp() {
        correctionMapper = new CorrectionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(correctionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(correctionMapper.fromId(null)).isNull();
    }
}

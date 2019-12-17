package io.hexlet.hexletcorrection.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CorrecterMapperTest {

    private CorrecterMapper correcterMapper;

    @BeforeEach
    public void setUp() {
        correcterMapper = new CorrecterMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(correcterMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(correcterMapper.fromId(null)).isNull();
    }
}

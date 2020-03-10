package io.hexlet.hexletcorrection.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PreferenceMapperTest {

    private PreferenceMapper preferenceMapper;

    @BeforeEach
    public void setUp() {
        preferenceMapper = new PreferenceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(preferenceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(preferenceMapper.fromId(null)).isNull();
    }
}

package io.hexlet.hexletcorrection.service.dto;

import io.hexlet.hexletcorrection.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PreferenceDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PreferenceDTO.class);
        PreferenceDTO preferenceDTO1 = new PreferenceDTO();
        preferenceDTO1.setId(1L);
        PreferenceDTO preferenceDTO2 = new PreferenceDTO();
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
        preferenceDTO2.setId(preferenceDTO1.getId());
        assertThat(preferenceDTO1).isEqualTo(preferenceDTO2);
        preferenceDTO2.setId(2L);
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
        preferenceDTO1.setId(null);
        assertThat(preferenceDTO1).isNotEqualTo(preferenceDTO2);
    }
}

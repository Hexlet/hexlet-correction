package io.hexlet.hexletcorrection.domain;

import io.hexlet.hexletcorrection.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PreferenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Preference.class);
        Preference preference1 = new Preference();
        preference1.setId(1L);
        Preference preference2 = new Preference();
        preference2.setId(preference1.getId());
        assertThat(preference1).isEqualTo(preference2);
        preference2.setId(2L);
        assertThat(preference1).isNotEqualTo(preference2);
        preference1.setId(null);
        assertThat(preference1).isNotEqualTo(preference2);
    }
}

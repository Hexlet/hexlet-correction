package io.hexlet.hexletcorrection.domain;

import io.hexlet.hexletcorrection.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CorrectionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Correction.class);
        Correction correction1 = new Correction();
        correction1.setId(1L);
        Correction correction2 = new Correction();
        correction2.setId(correction1.getId());
        assertThat(correction1).isEqualTo(correction2);
        correction2.setId(2L);
        assertThat(correction1).isNotEqualTo(correction2);
        correction1.setId(null);
        assertThat(correction1).isNotEqualTo(correction2);
    }
}

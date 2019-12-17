package io.hexlet.hexletcorrection.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.hexlet.hexletcorrection.web.rest.TestUtil;

public class CorrecterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Correcter.class);
        Correcter correcter1 = new Correcter();
        correcter1.setId(1L);
        Correcter correcter2 = new Correcter();
        correcter2.setId(correcter1.getId());
        assertThat(correcter1).isEqualTo(correcter2);
        correcter2.setId(2L);
        assertThat(correcter1).isNotEqualTo(correcter2);
        correcter1.setId(null);
        assertThat(correcter1).isNotEqualTo(correcter2);
    }
}

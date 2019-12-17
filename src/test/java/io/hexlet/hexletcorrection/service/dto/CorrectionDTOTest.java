package io.hexlet.hexletcorrection.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.hexlet.hexletcorrection.web.rest.TestUtil;

public class CorrectionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrectionDTO.class);
        CorrectionDTO correctionDTO1 = new CorrectionDTO();
        correctionDTO1.setId(1L);
        CorrectionDTO correctionDTO2 = new CorrectionDTO();
        assertThat(correctionDTO1).isNotEqualTo(correctionDTO2);
        correctionDTO2.setId(correctionDTO1.getId());
        assertThat(correctionDTO1).isEqualTo(correctionDTO2);
        correctionDTO2.setId(2L);
        assertThat(correctionDTO1).isNotEqualTo(correctionDTO2);
        correctionDTO1.setId(null);
        assertThat(correctionDTO1).isNotEqualTo(correctionDTO2);
    }
}

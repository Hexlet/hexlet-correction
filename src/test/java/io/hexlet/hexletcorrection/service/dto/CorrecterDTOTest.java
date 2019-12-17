package io.hexlet.hexletcorrection.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.hexlet.hexletcorrection.web.rest.TestUtil;

public class CorrecterDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrecterDTO.class);
        CorrecterDTO correcterDTO1 = new CorrecterDTO();
        correcterDTO1.setId(1L);
        CorrecterDTO correcterDTO2 = new CorrecterDTO();
        assertThat(correcterDTO1).isNotEqualTo(correcterDTO2);
        correcterDTO2.setId(correcterDTO1.getId());
        assertThat(correcterDTO1).isEqualTo(correcterDTO2);
        correcterDTO2.setId(2L);
        assertThat(correcterDTO1).isNotEqualTo(correcterDTO2);
        correcterDTO1.setId(null);
        assertThat(correcterDTO1).isNotEqualTo(correcterDTO2);
    }
}

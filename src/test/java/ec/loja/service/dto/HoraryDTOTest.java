package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoraryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HoraryDTO.class);
        HoraryDTO horaryDTO1 = new HoraryDTO();
        horaryDTO1.setId(1L);
        HoraryDTO horaryDTO2 = new HoraryDTO();
        assertThat(horaryDTO1).isNotEqualTo(horaryDTO2);
        horaryDTO2.setId(horaryDTO1.getId());
        assertThat(horaryDTO1).isEqualTo(horaryDTO2);
        horaryDTO2.setId(2L);
        assertThat(horaryDTO1).isNotEqualTo(horaryDTO2);
        horaryDTO1.setId(null);
        assertThat(horaryDTO1).isNotEqualTo(horaryDTO2);
    }
}

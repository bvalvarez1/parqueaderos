package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TariffDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TariffDTO.class);
        TariffDTO tariffDTO1 = new TariffDTO();
        tariffDTO1.setId(1L);
        TariffDTO tariffDTO2 = new TariffDTO();
        assertThat(tariffDTO1).isNotEqualTo(tariffDTO2);
        tariffDTO2.setId(tariffDTO1.getId());
        assertThat(tariffDTO1).isEqualTo(tariffDTO2);
        tariffDTO2.setId(2L);
        assertThat(tariffDTO1).isNotEqualTo(tariffDTO2);
        tariffDTO1.setId(null);
        assertThat(tariffDTO1).isNotEqualTo(tariffDTO2);
    }
}

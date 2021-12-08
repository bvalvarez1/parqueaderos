package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunctionalityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionalityDTO.class);
        FunctionalityDTO functionalityDTO1 = new FunctionalityDTO();
        functionalityDTO1.setId(1L);
        FunctionalityDTO functionalityDTO2 = new FunctionalityDTO();
        assertThat(functionalityDTO1).isNotEqualTo(functionalityDTO2);
        functionalityDTO2.setId(functionalityDTO1.getId());
        assertThat(functionalityDTO1).isEqualTo(functionalityDTO2);
        functionalityDTO2.setId(2L);
        assertThat(functionalityDTO1).isNotEqualTo(functionalityDTO2);
        functionalityDTO1.setId(null);
        assertThat(functionalityDTO1).isNotEqualTo(functionalityDTO2);
    }
}

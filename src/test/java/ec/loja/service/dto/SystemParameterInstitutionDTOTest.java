package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemParameterInstitutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemParameterInstitutionDTO.class);
        SystemParameterInstitutionDTO systemParameterInstitutionDTO1 = new SystemParameterInstitutionDTO();
        systemParameterInstitutionDTO1.setId(1L);
        SystemParameterInstitutionDTO systemParameterInstitutionDTO2 = new SystemParameterInstitutionDTO();
        assertThat(systemParameterInstitutionDTO1).isNotEqualTo(systemParameterInstitutionDTO2);
        systemParameterInstitutionDTO2.setId(systemParameterInstitutionDTO1.getId());
        assertThat(systemParameterInstitutionDTO1).isEqualTo(systemParameterInstitutionDTO2);
        systemParameterInstitutionDTO2.setId(2L);
        assertThat(systemParameterInstitutionDTO1).isNotEqualTo(systemParameterInstitutionDTO2);
        systemParameterInstitutionDTO1.setId(null);
        assertThat(systemParameterInstitutionDTO1).isNotEqualTo(systemParameterInstitutionDTO2);
    }
}

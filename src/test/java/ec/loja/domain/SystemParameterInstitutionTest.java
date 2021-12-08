package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemParameterInstitutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemParameterInstitution.class);
        SystemParameterInstitution systemParameterInstitution1 = new SystemParameterInstitution();
        systemParameterInstitution1.setId(1L);
        SystemParameterInstitution systemParameterInstitution2 = new SystemParameterInstitution();
        systemParameterInstitution2.setId(systemParameterInstitution1.getId());
        assertThat(systemParameterInstitution1).isEqualTo(systemParameterInstitution2);
        systemParameterInstitution2.setId(2L);
        assertThat(systemParameterInstitution1).isNotEqualTo(systemParameterInstitution2);
        systemParameterInstitution1.setId(null);
        assertThat(systemParameterInstitution1).isNotEqualTo(systemParameterInstitution2);
    }
}

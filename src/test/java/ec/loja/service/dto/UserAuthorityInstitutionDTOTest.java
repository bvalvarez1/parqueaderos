package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAuthorityInstitutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAuthorityInstitutionDTO.class);
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO1 = new UserAuthorityInstitutionDTO();
        userAuthorityInstitutionDTO1.setId(1L);
        UserAuthorityInstitutionDTO userAuthorityInstitutionDTO2 = new UserAuthorityInstitutionDTO();
        assertThat(userAuthorityInstitutionDTO1).isNotEqualTo(userAuthorityInstitutionDTO2);
        userAuthorityInstitutionDTO2.setId(userAuthorityInstitutionDTO1.getId());
        assertThat(userAuthorityInstitutionDTO1).isEqualTo(userAuthorityInstitutionDTO2);
        userAuthorityInstitutionDTO2.setId(2L);
        assertThat(userAuthorityInstitutionDTO1).isNotEqualTo(userAuthorityInstitutionDTO2);
        userAuthorityInstitutionDTO1.setId(null);
        assertThat(userAuthorityInstitutionDTO1).isNotEqualTo(userAuthorityInstitutionDTO2);
    }
}

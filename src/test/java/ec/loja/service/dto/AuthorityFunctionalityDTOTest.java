package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorityFunctionalityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorityFunctionalityDTO.class);
        AuthorityFunctionalityDTO authorityFunctionalityDTO1 = new AuthorityFunctionalityDTO();
        authorityFunctionalityDTO1.setId(1L);
        AuthorityFunctionalityDTO authorityFunctionalityDTO2 = new AuthorityFunctionalityDTO();
        assertThat(authorityFunctionalityDTO1).isNotEqualTo(authorityFunctionalityDTO2);
        authorityFunctionalityDTO2.setId(authorityFunctionalityDTO1.getId());
        assertThat(authorityFunctionalityDTO1).isEqualTo(authorityFunctionalityDTO2);
        authorityFunctionalityDTO2.setId(2L);
        assertThat(authorityFunctionalityDTO1).isNotEqualTo(authorityFunctionalityDTO2);
        authorityFunctionalityDTO1.setId(null);
        assertThat(authorityFunctionalityDTO1).isNotEqualTo(authorityFunctionalityDTO2);
    }
}

package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAuthorityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAuthorityDTO.class);
        UserAuthorityDTO userAuthorityDTO1 = new UserAuthorityDTO();
        userAuthorityDTO1.setId(1L);
        UserAuthorityDTO userAuthorityDTO2 = new UserAuthorityDTO();
        assertThat(userAuthorityDTO1).isNotEqualTo(userAuthorityDTO2);
        userAuthorityDTO2.setId(userAuthorityDTO1.getId());
        assertThat(userAuthorityDTO1).isEqualTo(userAuthorityDTO2);
        userAuthorityDTO2.setId(2L);
        assertThat(userAuthorityDTO1).isNotEqualTo(userAuthorityDTO2);
        userAuthorityDTO1.setId(null);
        assertThat(userAuthorityDTO1).isNotEqualTo(userAuthorityDTO2);
    }
}

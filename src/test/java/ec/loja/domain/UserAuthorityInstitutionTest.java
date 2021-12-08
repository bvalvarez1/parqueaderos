package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAuthorityInstitutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAuthorityInstitution.class);
        UserAuthorityInstitution userAuthorityInstitution1 = new UserAuthorityInstitution();
        userAuthorityInstitution1.setId(1L);
        UserAuthorityInstitution userAuthorityInstitution2 = new UserAuthorityInstitution();
        userAuthorityInstitution2.setId(userAuthorityInstitution1.getId());
        assertThat(userAuthorityInstitution1).isEqualTo(userAuthorityInstitution2);
        userAuthorityInstitution2.setId(2L);
        assertThat(userAuthorityInstitution1).isNotEqualTo(userAuthorityInstitution2);
        userAuthorityInstitution1.setId(null);
        assertThat(userAuthorityInstitution1).isNotEqualTo(userAuthorityInstitution2);
    }
}

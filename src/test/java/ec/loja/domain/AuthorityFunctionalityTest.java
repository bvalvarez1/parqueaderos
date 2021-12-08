package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuthorityFunctionalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorityFunctionality.class);
        AuthorityFunctionality authorityFunctionality1 = new AuthorityFunctionality();
        authorityFunctionality1.setId(1L);
        AuthorityFunctionality authorityFunctionality2 = new AuthorityFunctionality();
        authorityFunctionality2.setId(authorityFunctionality1.getId());
        assertThat(authorityFunctionality1).isEqualTo(authorityFunctionality2);
        authorityFunctionality2.setId(2L);
        assertThat(authorityFunctionality1).isNotEqualTo(authorityFunctionality2);
        authorityFunctionality1.setId(null);
        assertThat(authorityFunctionality1).isNotEqualTo(authorityFunctionality2);
    }
}

package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunctionalityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Functionality.class);
        Functionality functionality1 = new Functionality();
        functionality1.setId(1L);
        Functionality functionality2 = new Functionality();
        functionality2.setId(functionality1.getId());
        assertThat(functionality1).isEqualTo(functionality2);
        functionality2.setId(2L);
        assertThat(functionality1).isNotEqualTo(functionality2);
        functionality1.setId(null);
        assertThat(functionality1).isNotEqualTo(functionality2);
    }
}

package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HoraryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Horary.class);
        Horary horary1 = new Horary();
        horary1.setId(1L);
        Horary horary2 = new Horary();
        horary2.setId(horary1.getId());
        assertThat(horary1).isEqualTo(horary2);
        horary2.setId(2L);
        assertThat(horary1).isNotEqualTo(horary2);
        horary1.setId(null);
        assertThat(horary1).isNotEqualTo(horary2);
    }
}

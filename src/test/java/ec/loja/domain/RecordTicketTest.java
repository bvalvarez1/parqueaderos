package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordTicketTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordTicket.class);
        RecordTicket recordTicket1 = new RecordTicket();
        recordTicket1.setId(1L);
        RecordTicket recordTicket2 = new RecordTicket();
        recordTicket2.setId(recordTicket1.getId());
        assertThat(recordTicket1).isEqualTo(recordTicket2);
        recordTicket2.setId(2L);
        assertThat(recordTicket1).isNotEqualTo(recordTicket2);
        recordTicket1.setId(null);
        assertThat(recordTicket1).isNotEqualTo(recordTicket2);
    }
}

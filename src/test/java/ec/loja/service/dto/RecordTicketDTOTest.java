package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordTicketDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordTicketDTO.class);
        RecordTicketDTO recordTicketDTO1 = new RecordTicketDTO();
        recordTicketDTO1.setId(1L);
        RecordTicketDTO recordTicketDTO2 = new RecordTicketDTO();
        assertThat(recordTicketDTO1).isNotEqualTo(recordTicketDTO2);
        recordTicketDTO2.setId(recordTicketDTO1.getId());
        assertThat(recordTicketDTO1).isEqualTo(recordTicketDTO2);
        recordTicketDTO2.setId(2L);
        assertThat(recordTicketDTO1).isNotEqualTo(recordTicketDTO2);
        recordTicketDTO1.setId(null);
        assertThat(recordTicketDTO1).isNotEqualTo(recordTicketDTO2);
    }
}

package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecordDTO.class);
        RecordDTO recordDTO1 = new RecordDTO();
        recordDTO1.setId(1L);
        RecordDTO recordDTO2 = new RecordDTO();
        assertThat(recordDTO1).isNotEqualTo(recordDTO2);
        recordDTO2.setId(recordDTO1.getId());
        assertThat(recordDTO1).isEqualTo(recordDTO2);
        recordDTO2.setId(2L);
        assertThat(recordDTO1).isNotEqualTo(recordDTO2);
        recordDTO1.setId(null);
        assertThat(recordDTO1).isNotEqualTo(recordDTO2);
    }
}

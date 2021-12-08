package ec.loja.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TariffVehicleTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TariffVehicleTypeDTO.class);
        TariffVehicleTypeDTO tariffVehicleTypeDTO1 = new TariffVehicleTypeDTO();
        tariffVehicleTypeDTO1.setId(1L);
        TariffVehicleTypeDTO tariffVehicleTypeDTO2 = new TariffVehicleTypeDTO();
        assertThat(tariffVehicleTypeDTO1).isNotEqualTo(tariffVehicleTypeDTO2);
        tariffVehicleTypeDTO2.setId(tariffVehicleTypeDTO1.getId());
        assertThat(tariffVehicleTypeDTO1).isEqualTo(tariffVehicleTypeDTO2);
        tariffVehicleTypeDTO2.setId(2L);
        assertThat(tariffVehicleTypeDTO1).isNotEqualTo(tariffVehicleTypeDTO2);
        tariffVehicleTypeDTO1.setId(null);
        assertThat(tariffVehicleTypeDTO1).isNotEqualTo(tariffVehicleTypeDTO2);
    }
}

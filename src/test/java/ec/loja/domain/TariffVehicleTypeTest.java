package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TariffVehicleTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TariffVehicleType.class);
        TariffVehicleType tariffVehicleType1 = new TariffVehicleType();
        tariffVehicleType1.setId(1L);
        TariffVehicleType tariffVehicleType2 = new TariffVehicleType();
        tariffVehicleType2.setId(tariffVehicleType1.getId());
        assertThat(tariffVehicleType1).isEqualTo(tariffVehicleType2);
        tariffVehicleType2.setId(2L);
        assertThat(tariffVehicleType1).isNotEqualTo(tariffVehicleType2);
        tariffVehicleType1.setId(null);
        assertThat(tariffVehicleType1).isNotEqualTo(tariffVehicleType2);
    }
}

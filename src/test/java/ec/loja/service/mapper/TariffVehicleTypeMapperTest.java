package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TariffVehicleTypeMapperTest {

    private TariffVehicleTypeMapper tariffVehicleTypeMapper;

    @BeforeEach
    public void setUp() {
        tariffVehicleTypeMapper = new TariffVehicleTypeMapperImpl();
    }
}

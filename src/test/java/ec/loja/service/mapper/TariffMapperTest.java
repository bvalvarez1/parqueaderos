package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TariffMapperTest {

    private TariffMapper tariffMapper;

    @BeforeEach
    public void setUp() {
        tariffMapper = new TariffMapperImpl();
    }
}

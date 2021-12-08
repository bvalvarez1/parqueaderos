package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HoraryMapperTest {

    private HoraryMapper horaryMapper;

    @BeforeEach
    public void setUp() {
        horaryMapper = new HoraryMapperImpl();
    }
}

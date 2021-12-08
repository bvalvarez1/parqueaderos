package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemParametersMapperTest {

    private SystemParametersMapper systemParametersMapper;

    @BeforeEach
    public void setUp() {
        systemParametersMapper = new SystemParametersMapperImpl();
    }
}

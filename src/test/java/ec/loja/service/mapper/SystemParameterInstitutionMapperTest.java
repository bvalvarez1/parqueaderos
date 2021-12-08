package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemParameterInstitutionMapperTest {

    private SystemParameterInstitutionMapper systemParameterInstitutionMapper;

    @BeforeEach
    public void setUp() {
        systemParameterInstitutionMapper = new SystemParameterInstitutionMapperImpl();
    }
}

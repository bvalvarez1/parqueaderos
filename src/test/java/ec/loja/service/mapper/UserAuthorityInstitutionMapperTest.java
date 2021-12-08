package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAuthorityInstitutionMapperTest {

    private UserAuthorityInstitutionMapper userAuthorityInstitutionMapper;

    @BeforeEach
    public void setUp() {
        userAuthorityInstitutionMapper = new UserAuthorityInstitutionMapperImpl();
    }
}

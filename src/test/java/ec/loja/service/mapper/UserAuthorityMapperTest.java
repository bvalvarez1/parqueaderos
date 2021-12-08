package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserAuthorityMapperTest {

    private UserAuthorityMapper userAuthorityMapper;

    @BeforeEach
    public void setUp() {
        userAuthorityMapper = new UserAuthorityMapperImpl();
    }
}

package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthorityFunctionalityMapperTest {

    private AuthorityFunctionalityMapper authorityFunctionalityMapper;

    @BeforeEach
    public void setUp() {
        authorityFunctionalityMapper = new AuthorityFunctionalityMapperImpl();
    }
}

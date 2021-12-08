package ec.loja.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemCatalogueMapperTest {

    private ItemCatalogueMapper itemCatalogueMapper;

    @BeforeEach
    public void setUp() {
        itemCatalogueMapper = new ItemCatalogueMapperImpl();
    }
}

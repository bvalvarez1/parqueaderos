package ec.loja.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ec.loja.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemCatalogueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCatalogue.class);
        ItemCatalogue itemCatalogue1 = new ItemCatalogue();
        itemCatalogue1.setId(1L);
        ItemCatalogue itemCatalogue2 = new ItemCatalogue();
        itemCatalogue2.setId(itemCatalogue1.getId());
        assertThat(itemCatalogue1).isEqualTo(itemCatalogue2);
        itemCatalogue2.setId(2L);
        assertThat(itemCatalogue1).isNotEqualTo(itemCatalogue2);
        itemCatalogue1.setId(null);
        assertThat(itemCatalogue1).isNotEqualTo(itemCatalogue2);
    }
}

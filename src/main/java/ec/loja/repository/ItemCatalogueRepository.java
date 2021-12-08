package ec.loja.repository;

import ec.loja.domain.ItemCatalogue;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemCatalogue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemCatalogueRepository extends JpaRepository<ItemCatalogue, Long> {
    @Query("Select ic from ItemCatalogue ic where ic.code=?1 and ic.catalogCode=?2")
    public Optional<ItemCatalogue> findByCodeAndCatalogCode(String code, String catalogCode);
}

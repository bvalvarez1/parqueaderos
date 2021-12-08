package ec.loja.service;

import ec.loja.service.dto.ItemCatalogueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.ItemCatalogue}.
 */
public interface ItemCatalogueService {
    /**
     * Save a itemCatalogue.
     *
     * @param itemCatalogueDTO the entity to save.
     * @return the persisted entity.
     */
    ItemCatalogueDTO save(ItemCatalogueDTO itemCatalogueDTO);

    /**
     * Partially updates a itemCatalogue.
     *
     * @param itemCatalogueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemCatalogueDTO> partialUpdate(ItemCatalogueDTO itemCatalogueDTO);

    /**
     * Get all the itemCatalogues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ItemCatalogueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" itemCatalogue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemCatalogueDTO> findOne(Long id);

    /**
     * Delete the "id" itemCatalogue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * encontrar por codigo y catalog code
     * @param code
     * @param catalogCode
     * @return
     */
    Optional<ItemCatalogueDTO> findByCodeAndCatalog(String code, String catalogCode);
}

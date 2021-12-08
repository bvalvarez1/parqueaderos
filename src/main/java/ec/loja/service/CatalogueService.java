package ec.loja.service;

import ec.loja.service.dto.CatalogueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.Catalogue}.
 */
public interface CatalogueService {
    /**
     * Save a catalogue.
     *
     * @param catalogueDTO the entity to save.
     * @return the persisted entity.
     */
    CatalogueDTO save(CatalogueDTO catalogueDTO);

    /**
     * Partially updates a catalogue.
     *
     * @param catalogueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CatalogueDTO> partialUpdate(CatalogueDTO catalogueDTO);

    /**
     * Get all the catalogues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CatalogueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" catalogue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CatalogueDTO> findOne(Long id);

    /**
     * Delete the "id" catalogue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

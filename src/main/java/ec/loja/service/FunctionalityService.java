package ec.loja.service;

import ec.loja.service.dto.FunctionalityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.Functionality}.
 */
public interface FunctionalityService {
    /**
     * Save a functionality.
     *
     * @param functionalityDTO the entity to save.
     * @return the persisted entity.
     */
    FunctionalityDTO save(FunctionalityDTO functionalityDTO);

    /**
     * Partially updates a functionality.
     *
     * @param functionalityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FunctionalityDTO> partialUpdate(FunctionalityDTO functionalityDTO);

    /**
     * Get all the functionalities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FunctionalityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" functionality.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FunctionalityDTO> findOne(Long id);

    /**
     * Delete the "id" functionality.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

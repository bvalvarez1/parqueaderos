package ec.loja.service;

import ec.loja.service.dto.SystemParametersDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.SystemParameters}.
 */
public interface SystemParametersService {
    /**
     * Save a systemParameters.
     *
     * @param systemParametersDTO the entity to save.
     * @return the persisted entity.
     */
    SystemParametersDTO save(SystemParametersDTO systemParametersDTO);

    /**
     * Partially updates a systemParameters.
     *
     * @param systemParametersDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemParametersDTO> partialUpdate(SystemParametersDTO systemParametersDTO);

    /**
     * Get all the systemParameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemParametersDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemParameters.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemParametersDTO> findOne(Long id);

    /**
     * Delete the "id" systemParameters.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

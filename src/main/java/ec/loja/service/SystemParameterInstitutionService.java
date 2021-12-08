package ec.loja.service;

import ec.loja.service.dto.SystemParameterInstitutionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.SystemParameterInstitution}.
 */
public interface SystemParameterInstitutionService {
    /**
     * Save a systemParameterInstitution.
     *
     * @param systemParameterInstitutionDTO the entity to save.
     * @return the persisted entity.
     */
    SystemParameterInstitutionDTO save(SystemParameterInstitutionDTO systemParameterInstitutionDTO);

    /**
     * Partially updates a systemParameterInstitution.
     *
     * @param systemParameterInstitutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SystemParameterInstitutionDTO> partialUpdate(SystemParameterInstitutionDTO systemParameterInstitutionDTO);

    /**
     * Get all the systemParameterInstitutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SystemParameterInstitutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" systemParameterInstitution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SystemParameterInstitutionDTO> findOne(Long id);

    /**
     * Delete the "id" systemParameterInstitution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package ec.loja.service;

import ec.loja.service.dto.UserAuthorityInstitutionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.UserAuthorityInstitution}.
 */
public interface UserAuthorityInstitutionService {
    /**
     * Save a userAuthorityInstitution.
     *
     * @param userAuthorityInstitutionDTO the entity to save.
     * @return the persisted entity.
     */
    UserAuthorityInstitutionDTO save(UserAuthorityInstitutionDTO userAuthorityInstitutionDTO);

    /**
     * Partially updates a userAuthorityInstitution.
     *
     * @param userAuthorityInstitutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAuthorityInstitutionDTO> partialUpdate(UserAuthorityInstitutionDTO userAuthorityInstitutionDTO);

    /**
     * Get all the userAuthorityInstitutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAuthorityInstitutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userAuthorityInstitution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAuthorityInstitutionDTO> findOne(Long id);

    /**
     * Delete the "id" userAuthorityInstitution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package ec.loja.service;

import ec.loja.service.dto.AuthorityFunctionalityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.AuthorityFunctionality}.
 */
public interface AuthorityFunctionalityService {
    /**
     * Save a authorityFunctionality.
     *
     * @param authorityFunctionalityDTO the entity to save.
     * @return the persisted entity.
     */
    AuthorityFunctionalityDTO save(AuthorityFunctionalityDTO authorityFunctionalityDTO);

    /**
     * Partially updates a authorityFunctionality.
     *
     * @param authorityFunctionalityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AuthorityFunctionalityDTO> partialUpdate(AuthorityFunctionalityDTO authorityFunctionalityDTO);

    /**
     * Get all the authorityFunctionalities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AuthorityFunctionalityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" authorityFunctionality.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AuthorityFunctionalityDTO> findOne(Long id);

    /**
     * Delete the "id" authorityFunctionality.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

package ec.loja.service;

import ec.loja.service.dto.JHIUserAuthorityDTO;
import ec.loja.service.dto.UserAuthorityDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.UserAuthority}.
 */
public interface UserAuthorityService {
    /**
     * Save a userAuthority.
     *
     * @param userAuthorityDTO the entity to save.
     * @return the persisted entity.
     */
    UserAuthorityDTO save(UserAuthorityDTO userAuthorityDTO);

    /**
     * Partially updates a userAuthority.
     *
     * @param userAuthorityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAuthorityDTO> partialUpdate(UserAuthorityDTO userAuthorityDTO);

    /**
     * Get all the userAuthorities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAuthorityDTO> findAll(Pageable pageable);

    /**
     * Get the "id" userAuthority.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAuthorityDTO> findOne(Long id);

    /**
     * Delete the "id" userAuthority.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     *
     * @param userid
     * @return
     */
    JHIUserAuthorityDTO findByUserId(Long userid);
}

package ec.loja.service;

import ec.loja.service.dto.TariffDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.Tariff}.
 */
public interface TariffService {
    /**
     * Save a tariff.
     *
     * @param tariffDTO the entity to save.
     * @return the persisted entity.
     */
    TariffDTO save(TariffDTO tariffDTO);

    /**
     * Partially updates a tariff.
     *
     * @param tariffDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TariffDTO> partialUpdate(TariffDTO tariffDTO);

    /**
     * Get all the tariffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TariffDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tariff.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TariffDTO> findOne(Long id);

    /**
     * Delete the "id" tariff.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

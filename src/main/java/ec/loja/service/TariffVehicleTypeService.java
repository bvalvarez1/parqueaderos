package ec.loja.service;

import ec.loja.domain.TariffVehicleType;
import ec.loja.service.dto.TariffVehicleTypeDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.TariffVehicleType}.
 */
public interface TariffVehicleTypeService {
    /**
     * Save a tariffVehicleType.
     *
     * @param tariffVehicleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    TariffVehicleTypeDTO save(TariffVehicleTypeDTO tariffVehicleTypeDTO);

    /**
     * Partially updates a tariffVehicleType.
     *
     * @param tariffVehicleTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TariffVehicleTypeDTO> partialUpdate(TariffVehicleTypeDTO tariffVehicleTypeDTO);

    /**
     * Get all the tariffVehicleTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TariffVehicleTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tariffVehicleType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TariffVehicleTypeDTO> findOne(Long id);

    /**
     * Delete the "id" tariffVehicleType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     *
     * @param institutionid
     * @param currentdate
     * @param vehicletypeid
     * @return
     */
    List<TariffVehicleType> getTarrifVehicleByInstitution(Long institutionid, LocalDate currentdate);
}

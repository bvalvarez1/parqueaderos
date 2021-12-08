package ec.loja.service;

import ec.loja.service.dto.PlaceDTO;
import ec.loja.service.dto.PlaceStatusDTO;
import ec.loja.service.dto.PlaceTicketsDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.Place}.
 */
public interface PlaceService {
    /**
     * Save a place.
     *
     * @param placeDTO the entity to save.
     * @return the persisted entity.
     */
    PlaceDTO save(PlaceDTO placeDTO);

    /**
     * Partially updates a place.
     *
     * @param placeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaceDTO> partialUpdate(PlaceDTO placeDTO);

    /**
     * Get all the places.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" place.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaceDTO> findOne(Long id);

    /**
     * Delete the "id" place.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Obtener las plazas libres y ocupadas
     * @param institutionid
     * @return List<PlaceTicketsDTO>
     */
    List<PlaceTicketsDTO> getPlaces(Long institutionid);

    /**
     * Total de lugares ocupados o libres
     * @param catalogPlace
     * @param userid
     * @return
     */
    List<PlaceStatusDTO> getPlacesByStatus(String catalogPlace, Long userid);

    /**
     *
     * @param institutionid
     * @param statusid
     * @return
     */
    Optional<PlaceDTO> getAvailablePlace(Long institutionid, Long statusid);
}

package ec.loja.service;

import ec.loja.service.dto.InstitutionDTO;
import ec.loja.service.dto.InstitutionParkingDTO;
import ec.loja.service.dto.PlacesFreeDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.Institution}.
 */
public interface InstitutionService {
    /**
     * Save a institution.
     *
     * @param institutionDTO the entity to save.
     * @return the persisted entity.
     */
    InstitutionDTO save(InstitutionDTO institutionDTO);

    /**
     * Partially updates a institution.
     *
     * @param institutionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InstitutionDTO> partialUpdate(InstitutionDTO institutionDTO);

    /**
     * Get all the institutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InstitutionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" institution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InstitutionDTO> findOne(Long id);

    /**
     * Delete the "id" institution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Obtener la institution por el id de usuario
     * @param userid
     * @return String
     */
    Optional<InstitutionParkingDTO> getByUser(Long userid);

    /**
     * Generar un nuevo valor de secuencia
     * @param sequenceName
     * @return String
     */
    String serialTicket(String sequenceName);

    /**
     *
     * @param latitudStart
     * @param latitudEnd
     * @param longitudStart
     * @param longitudEnd
     * @param statusplace
     * @return
     */
    List<PlacesFreeDTO> getFreeLocations(
        BigDecimal latitudeStart,
        BigDecimal latitudeEnd,
        BigDecimal longitudeStart,
        BigDecimal longitudeEnd,
        String statusplace
    );

    /**
     *
     * @param total
     * @param institutionid
     */
    void createPlaces(int total, Long institutionid);

    /**
     *
     * @param sequencename
     */
    void createSequence(String sequencename);
}

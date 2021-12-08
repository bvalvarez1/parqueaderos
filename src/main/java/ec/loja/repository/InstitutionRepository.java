package ec.loja.repository;

import ec.loja.domain.Institution;
import ec.loja.service.dto.InstitutionDTO;
import ec.loja.service.dto.InstitutionParkingDTO;
import ec.loja.service.dto.PlacesFreeDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Institution entity.
 */
@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
    @Query(
        value = "select i.id, i.name, i.address, i.places_number as placeNumber, i.ruc, i.latitude, i.longitude, i.sequencename, i.acronym " +
        "FROM institution i " +
        "join user_authority_institution uai on uai.institution_id = i.id " +
        "join user_authority ua on ua.id = uai.user_authority_id " +
        "join jhi_user ju on ju.id = ua.user_id " +
        "where ju.id=?1",
        nativeQuery = true
    )
    Optional<InstitutionParkingDTO> getNameByCashier(Long userid);

    @Query(value = "select * from getSequenceNumber(?1)", nativeQuery = true)
    Long getSerialInstitution(String sequencename);

    @Query(
        value = "select i.id, i.name, i.sequencename, i.latitude, i.longitude, " +
        "count(p.*) as availables " +
        "from place p " +
        "join institution i on i.id = p.institution_id " +
        "join item_catalogue ic on ic.id = p.status_id " +
        "where i.latitude between :latitudeStart  and :latitudeEnd " +
        "and i.longitude between :longitudeStart and :longitudeEnd " +
        "and ic.code=:statusplace " +
        "group by i.id, i.name, i.sequencename, i.latitude, i.longitude " +
        "having count(p.*)>0",
        nativeQuery = true
    )
    List<PlacesFreeDTO> getFreePlaces(
        @Param("latitudeStart") BigDecimal latitudeStart,
        @Param("latitudeEnd") BigDecimal latitudeEnd,
        @Param("longitudeStart") BigDecimal longitudeStart,
        @Param("longitudeEnd") BigDecimal longitudeEnd,
        @Param("statusplace") String statusplace
    );

    @Query(value = "select * from public.createplaceinstitution(?1, ?2)", nativeQuery = true)
    Integer createPlacesForInstitution(int total, Long institutionid);

    @Query(value = "select * from public.createsequenceinstitution(?1)", nativeQuery = true)
    Long createSequence(String sequencename);
}

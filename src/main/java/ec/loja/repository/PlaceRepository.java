package ec.loja.repository;

import ec.loja.domain.Place;
import ec.loja.service.dto.PlaceDTO;
import ec.loja.service.dto.PlaceStatusDTO;
import ec.loja.service.dto.PlaceTicketsDTO;
import java.util.List;
import java.util.Optional;
import javax.websocket.server.PathParam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Place entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query(value = "select * from placesbyinstitution(?1)", nativeQuery = true)
    List<PlaceTicketsDTO> getPlaces(Long institutionid);

    @Query(
        value = "select ic.name status, count(p.*) as total " +
        "FROM place p " +
        "JOIN user_authority_institution uai on uai.institution_id  = p.institution_id " +
        "JOIN user_authority ua on ua.id  = uai.user_authority_id " +
        "LEFT JOIN item_catalogue ic on p.status_id = ic.id " +
        "WHERE  ic.catalog_code =?1 " +
        "and ua.user_id = ?2 " +
        "group by 1",
        nativeQuery = true
    )
    List<PlaceStatusDTO> getStatusPlace(String catalogCode, Long userid);

    @Query("Select p " + " FROM Place p " + " WHERE p.institution.id=?1 and p.status.id=?2 " + " ORDER BY cast(p.number as integer) ")
    List<Place> getPlace(Long instititutionid, Long statusid);
}

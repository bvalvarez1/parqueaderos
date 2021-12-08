package ec.loja.repository;

import ec.loja.domain.Tariff;
import ec.loja.domain.TariffVehicleType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TariffVehicleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TariffVehicleTypeRepository extends JpaRepository<TariffVehicleType, Long> {
    @Query(
        "Select tv " +
        "FROM TariffVehicleType tv " +
        "WHERE tv.tariff.institution.id=?1 and " +
        "?2 between tv.tariff.initialDate and tv.tariff.finalDate"
    )
    public List<TariffVehicleType> getByInstitution(Long institutionid, LocalDate current);
}

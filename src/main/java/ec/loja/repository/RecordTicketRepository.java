package ec.loja.repository;

import ec.loja.domain.RecordTicket;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RecordTicket entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordTicketRepository extends JpaRepository<RecordTicket, Long> {
    @Query("Select rt from RecordTicket rt where rt.sequential=:sequential and rt.institution.id=:institutionid")
    RecordTicket findBySerianAndInstitution(@Param("sequential") String sequential, @Param("institutionid") Long institutionid);
}

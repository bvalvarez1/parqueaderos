package ec.loja.repository;

import ec.loja.domain.Record;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Record entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {}

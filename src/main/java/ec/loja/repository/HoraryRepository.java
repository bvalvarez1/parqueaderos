package ec.loja.repository;

import ec.loja.domain.Horary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Horary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoraryRepository extends JpaRepository<Horary, Long> {}

package ec.loja.repository;

import ec.loja.domain.Functionality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Functionality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunctionalityRepository extends JpaRepository<Functionality, Long> {}

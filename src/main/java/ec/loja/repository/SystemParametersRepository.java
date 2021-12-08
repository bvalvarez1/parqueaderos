package ec.loja.repository;

import ec.loja.domain.SystemParameters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemParameters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemParametersRepository extends JpaRepository<SystemParameters, Long> {}

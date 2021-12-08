package ec.loja.repository;

import ec.loja.domain.SystemParameterInstitution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SystemParameterInstitution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemParameterInstitutionRepository extends JpaRepository<SystemParameterInstitution, Long> {}

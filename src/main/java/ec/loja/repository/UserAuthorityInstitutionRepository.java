package ec.loja.repository;

import ec.loja.domain.UserAuthorityInstitution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAuthorityInstitution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAuthorityInstitutionRepository extends JpaRepository<UserAuthorityInstitution, Long> {}

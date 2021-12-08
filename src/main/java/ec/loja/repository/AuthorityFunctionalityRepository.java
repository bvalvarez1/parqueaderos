package ec.loja.repository;

import ec.loja.domain.AuthorityFunctionality;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AuthorityFunctionality entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorityFunctionalityRepository extends JpaRepository<AuthorityFunctionality, Long> {}

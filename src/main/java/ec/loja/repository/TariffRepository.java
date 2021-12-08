package ec.loja.repository;

import ec.loja.domain.Tariff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tariff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {}

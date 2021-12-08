package ec.loja.repository;

import ec.loja.domain.Person;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("select person from Person person where person.user.login = ?#{principal.username}")
    List<Person> findByUserIsCurrentUser();
}

package ec.loja.repository;

import ec.loja.domain.UserAuthority;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserAuthority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    @Query("select userAuthority from UserAuthority userAuthority where userAuthority.user.login = ?#{principal.username}")
    List<UserAuthority> findByUserIsCurrentUser();
}

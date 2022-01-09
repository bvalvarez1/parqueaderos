package ec.loja.repository;

import ec.loja.domain.UserAuthority;
import ec.loja.service.dto.JHIUserAuthorityDTO;
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

    @Query("select userAuthority from UserAuthority userAuthority where userAuthority.user.id = ?1")
    UserAuthority findByUserid(Long userid);

    @Query(
        value = "select user_id as userid, authority_name authorityname " + " from jhi_user_authority " + " where user_id = ?1",
        nativeQuery = true
    )
    JHIUserAuthorityDTO findUserAuthority(Long userid);
}

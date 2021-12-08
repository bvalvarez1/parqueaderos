package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.UserAuthorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAuthority} and its DTO {@link UserAuthorityDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserAuthorityMapper extends EntityMapper<UserAuthorityDTO, UserAuthority> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UserAuthorityDTO toDto(UserAuthority s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserAuthorityDTO toDtoId(UserAuthority userAuthority);
}

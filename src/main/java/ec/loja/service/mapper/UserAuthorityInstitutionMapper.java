package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.UserAuthorityInstitutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserAuthorityInstitution} and its DTO {@link UserAuthorityInstitutionDTO}.
 */
@Mapper(componentModel = "spring", uses = { InstitutionMapper.class, UserAuthorityMapper.class })
public interface UserAuthorityInstitutionMapper extends EntityMapper<UserAuthorityInstitutionDTO, UserAuthorityInstitution> {
    @Mapping(target = "institution", source = "institution", qualifiedByName = "name")
    @Mapping(target = "userAuthority", source = "userAuthority", qualifiedByName = "id")
    UserAuthorityInstitutionDTO toDto(UserAuthorityInstitution s);
}

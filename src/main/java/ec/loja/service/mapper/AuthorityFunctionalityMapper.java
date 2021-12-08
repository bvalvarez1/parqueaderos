package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.AuthorityFunctionalityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuthorityFunctionality} and its DTO {@link AuthorityFunctionalityDTO}.
 */
@Mapper(componentModel = "spring", uses = { FunctionalityMapper.class })
public interface AuthorityFunctionalityMapper extends EntityMapper<AuthorityFunctionalityDTO, AuthorityFunctionality> {
    @Mapping(target = "functionality", source = "functionality", qualifiedByName = "name")
    AuthorityFunctionalityDTO toDto(AuthorityFunctionality s);
}

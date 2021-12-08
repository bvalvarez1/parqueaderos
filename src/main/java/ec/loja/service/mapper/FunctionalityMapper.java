package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.FunctionalityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Functionality} and its DTO {@link FunctionalityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FunctionalityMapper extends EntityMapper<FunctionalityDTO, Functionality> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "id")
    FunctionalityDTO toDto(Functionality s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FunctionalityDTO toDtoId(Functionality functionality);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    FunctionalityDTO toDtoName(Functionality functionality);
}

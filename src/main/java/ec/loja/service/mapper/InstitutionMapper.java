package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.InstitutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Institution} and its DTO {@link InstitutionDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemCatalogueMapper.class })
public interface InstitutionMapper extends EntityMapper<InstitutionDTO, Institution> {
    @Mapping(target = "canton", source = "canton", qualifiedByName = "name")
    InstitutionDTO toDto(Institution s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InstitutionDTO toDtoId(Institution institution);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    InstitutionDTO toDtoName(Institution institution);
}

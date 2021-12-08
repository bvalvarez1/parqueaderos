package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.PlaceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Place} and its DTO {@link PlaceDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemCatalogueMapper.class, InstitutionMapper.class })
public interface PlaceMapper extends EntityMapper<PlaceDTO, Place> {
    @Mapping(target = "status", source = "status", qualifiedByName = "name")
    @Mapping(target = "institution", source = "institution", qualifiedByName = "id")
    PlaceDTO toDto(Place s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlaceDTO toDtoId(Place place);
}

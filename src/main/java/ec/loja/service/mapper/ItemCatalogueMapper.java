package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.ItemCatalogueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemCatalogue} and its DTO {@link ItemCatalogueDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogueMapper.class })
public interface ItemCatalogueMapper extends EntityMapper<ItemCatalogueDTO, ItemCatalogue> {
    @Mapping(target = "catalogue", source = "catalogue", qualifiedByName = "id")
    ItemCatalogueDTO toDto(ItemCatalogue s);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ItemCatalogueDTO toDtoName(ItemCatalogue itemCatalogue);
}

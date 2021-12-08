package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.CatalogueDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Catalogue} and its DTO {@link CatalogueDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatalogueMapper extends EntityMapper<CatalogueDTO, Catalogue> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CatalogueDTO toDtoId(Catalogue catalogue);
}

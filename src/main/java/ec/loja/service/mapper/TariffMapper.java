package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.TariffDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tariff} and its DTO {@link TariffDTO}.
 */
@Mapper(componentModel = "spring", uses = { InstitutionMapper.class })
public interface TariffMapper extends EntityMapper<TariffDTO, Tariff> {
    @Mapping(target = "institution", source = "institution", qualifiedByName = "name")
    TariffDTO toDto(Tariff s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TariffDTO toDtoId(Tariff tariff);
}

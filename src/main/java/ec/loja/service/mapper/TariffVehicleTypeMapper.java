package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.TariffVehicleTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TariffVehicleType} and its DTO {@link TariffVehicleTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemCatalogueMapper.class, TariffMapper.class })
public interface TariffVehicleTypeMapper extends EntityMapper<TariffVehicleTypeDTO, TariffVehicleType> {
    @Mapping(target = "vehicleType", source = "vehicleType", qualifiedByName = "name")
    @Mapping(target = "tariff", source = "tariff", qualifiedByName = "id")
    TariffVehicleTypeDTO toDto(TariffVehicleType s);

    @Named("maxValue")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "maxValue", source = "maxValue")
    TariffVehicleTypeDTO toDtoMaxValue(TariffVehicleType tariffVehicleType);
}

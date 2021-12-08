package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.RecordTicketDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RecordTicket} and its DTO {@link RecordTicketDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { PlaceMapper.class, UserMapper.class, ItemCatalogueMapper.class, TariffVehicleTypeMapper.class, InstitutionMapper.class }
)
public interface RecordTicketMapper extends EntityMapper<RecordTicketDTO, RecordTicket> {
    @Mapping(target = "placeid", source = "placeid", qualifiedByName = "id")
    @Mapping(target = "emitter", source = "emitter", qualifiedByName = "id")
    @Mapping(target = "collector", source = "collector", qualifiedByName = "id")
    @Mapping(target = "status", source = "status", qualifiedByName = "name")
    @Mapping(target = "tariffVehicle", source = "tariffVehicle", qualifiedByName = "maxValue")
    @Mapping(target = "institution", source = "institution", qualifiedByName = "name")
    RecordTicketDTO toDto(RecordTicket s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RecordTicketDTO toDtoId(RecordTicket recordTicket);
}

package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.HoraryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Horary} and its DTO {@link HoraryDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemCatalogueMapper.class, ContractMapper.class })
public interface HoraryMapper extends EntityMapper<HoraryDTO, Horary> {
    @Mapping(target = "status", source = "status", qualifiedByName = "name")
    @Mapping(target = "contract", source = "contract", qualifiedByName = "id")
    HoraryDTO toDto(Horary s);
}

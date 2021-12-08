package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.ContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring", uses = { ItemCatalogueMapper.class, PersonMapper.class })
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {
    @Mapping(target = "status", source = "status", qualifiedByName = "name")
    @Mapping(target = "contractor", source = "contractor", qualifiedByName = "fullName")
    ContractDTO toDto(Contract s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractDTO toDtoId(Contract contract);
}

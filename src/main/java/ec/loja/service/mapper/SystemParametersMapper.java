package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.SystemParametersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemParameters} and its DTO {@link SystemParametersDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SystemParametersMapper extends EntityMapper<SystemParametersDTO, SystemParameters> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SystemParametersDTO toDtoName(SystemParameters systemParameters);
}

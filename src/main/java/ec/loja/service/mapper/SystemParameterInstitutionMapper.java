package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.SystemParameterInstitutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemParameterInstitution} and its DTO {@link SystemParameterInstitutionDTO}.
 */
@Mapper(componentModel = "spring", uses = { SystemParametersMapper.class, InstitutionMapper.class })
public interface SystemParameterInstitutionMapper extends EntityMapper<SystemParameterInstitutionDTO, SystemParameterInstitution> {
    @Mapping(target = "parameter", source = "parameter", qualifiedByName = "name")
    @Mapping(target = "institution", source = "institution", qualifiedByName = "name")
    SystemParameterInstitutionDTO toDto(SystemParameterInstitution s);
}

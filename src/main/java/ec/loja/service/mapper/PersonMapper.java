package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.PersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ItemCatalogueMapper.class })
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "identificationType", source = "identificationType", qualifiedByName = "name")
    PersonDTO toDto(Person s);

    @Named("fullName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "fullName", source = "fullName")
    PersonDTO toDtoFullName(Person person);
}

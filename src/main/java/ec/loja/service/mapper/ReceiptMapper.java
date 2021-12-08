package ec.loja.service.mapper;

import ec.loja.domain.*;
import ec.loja.service.dto.ReceiptDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Receipt} and its DTO {@link ReceiptDTO}.
 */
@Mapper(componentModel = "spring", uses = { RecordTicketMapper.class })
public interface ReceiptMapper extends EntityMapper<ReceiptDTO, Receipt> {
    @Mapping(target = "recordticketid", source = "recordticketid", qualifiedByName = "id")
    ReceiptDTO toDto(Receipt s);
}

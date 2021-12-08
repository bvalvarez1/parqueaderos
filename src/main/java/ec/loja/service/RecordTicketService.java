package ec.loja.service;

import ec.loja.service.dto.RecordTicketDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link ec.loja.domain.RecordTicket}.
 */
public interface RecordTicketService {
    /**
     * Save a recordTicket.
     *
     * @param recordTicketDTO the entity to save.
     * @return the persisted entity.
     */
    RecordTicketDTO save(RecordTicketDTO recordTicketDTO);

    /**
     * Partially updates a recordTicket.
     *
     * @param recordTicketDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecordTicketDTO> partialUpdate(RecordTicketDTO recordTicketDTO);

    /**
     * Get all the recordTickets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RecordTicketDTO> findAll(Pageable pageable);

    /**
     * Get the "id" recordTicket.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecordTicketDTO> findOne(Long id);

    /**
     * Delete the "id" recordTicket.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Obtener el ticket de acuerdo al secuencial e id de institucion
     * @param sequential
     * @param institutionid
     * @return RecordTicketDTO
     */
    Optional<RecordTicketDTO> findBySerialAndInstitution(String sequential, Long institutionid);
}
